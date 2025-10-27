package com.sampoom.backend.api.order.service;

import com.sampoom.backend.api.order.dto.*;
import com.sampoom.backend.api.order.entity.Order;
import com.sampoom.backend.api.order.entity.OrderStatus;
import com.sampoom.backend.api.order.entity.Requester;
import com.sampoom.backend.api.order.entity.EventOutbox;
import com.sampoom.backend.api.order.event.ToWarehouseEvent;
import com.sampoom.backend.api.order.repository.EventOutboxRepository;
import com.sampoom.backend.api.order.repository.OrderRepository;
import com.sampoom.backend.common.exception.BadRequestException;
import com.sampoom.backend.common.response.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderPartService orderPartService;
    private final EventOutboxRepository eventOutboxRepository;

    @Transactional
    public OrderResDto createOrder(OrderReqDto orderReqDto) {
        Order newOrder = Order.builder()
                .requester(orderReqDto.getRequester())
                .branch(orderReqDto.getBranch())
                .status(OrderStatus.PENDING)
                .build();
        orderRepository.saveAndFlush(newOrder);
        orderPartService.saveAllParts(newOrder.getId(), orderReqDto.getItems());

        EventOutbox newEventOutbox = EventOutbox.builder()
                .topic("sales-event")
                .payload(ToWarehouseEvent.builder()
                        .orderId(newOrder.getId())
                        .branch(orderReqDto.getBranch())
                        .items(orderReqDto.getItems())
                        .build())
                .build();
        eventOutboxRepository.saveAndFlush(newEventOutbox);

        return OrderResDto.builder()
                .id(newOrder.getId())
                .requester(orderReqDto.getRequester())
                .branch(orderReqDto.getBranch())
                .status(OrderStatus.PENDING)
                .items(orderReqDto.getItems())
                .build();
    }

    @Transactional(readOnly = true)
    public List<OrderResDto> getOrders(String from, String branch) {
        Requester requester;

        if (!StringUtils.hasText(from)) {
            throw new BadRequestException(ErrorStatus.NO_QUERY_PARAMETER.getMessage());
        }
        try {
            requester = Requester.valueOf(from.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException(ErrorStatus.INVALID_QUERY_PARAMETER.getMessage());
        }

        final String normalizedBranch = StringUtils.hasText(branch) ? branch.trim() : null;
        List<Order> orders = orderRepository.findWithItemsByRequesterAndBranch(requester, normalizedBranch);

        return orders.stream()
                .map(order -> OrderResDto.builder()
                        .id(order.getId())
                        .requester(order.getRequester())
                        .branch(order.getBranch())
                        .status(order.getStatus())
                        .items(order.getOrderParts().stream()
                                .map(op -> new ItemDto(op.getCode(), op.getQuantity()))
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
    }

}
