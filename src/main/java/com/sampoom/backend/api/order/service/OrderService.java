package com.sampoom.backend.api.order.service;

import com.sampoom.backend.api.order.dto.*;
import com.sampoom.backend.api.order.entity.Order;
import com.sampoom.backend.api.order.entity.OrderStatus;
import com.sampoom.backend.api.order.entity.EventOutbox;
import com.sampoom.backend.api.order.event.ToWarehouseEvent;
import com.sampoom.backend.api.order.repository.EventOutboxRepository;
import com.sampoom.backend.api.order.repository.OrderRepository;
import com.sampoom.backend.common.exception.NotFoundException;
import com.sampoom.backend.common.response.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.ZoneOffset;
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
                .branch(orderReqDto.getBranch())
                .status(OrderStatus.PENDING)
                .build();
        orderRepository.saveAndFlush(newOrder);
        orderPartService.saveAllParts(newOrder.getId(), orderReqDto.getItems());

        EventOutbox newEventOutbox = EventOutbox.builder()
                .topic("sales-events")
                .payload(ToWarehouseEvent.builder()
                        .orderId(newOrder.getId())
                        .branch(orderReqDto.getBranch())
                        .items(orderReqDto.getItems())
                        .version(newOrder.getVersion())
                        .sourceUpdatedAt(newOrder.getCreatedAt().atOffset(ZoneOffset.ofHours(9)))
                        .build())
                .build();
        eventOutboxRepository.saveAndFlush(newEventOutbox);

        return OrderResDto.builder()
                .id(newOrder.getId())
                .branch(orderReqDto.getBranch())
                .status(OrderStatus.PENDING)
                .items(orderReqDto.getItems())
                .build();
    }

    @Transactional(readOnly = true)
    public List<OrderResDto> getOrders(String from) {
        final String normalizedBranch = StringUtils.hasText(from) ? from.trim() : null;
        List<Order> orders = orderRepository.findWithItemsByBranch(normalizedBranch);

        return orders.stream()
                .map(order -> OrderResDto.builder()
                        .id(order.getId())
                        .branch(order.getBranch())
                        .status(order.getStatus())
                        .items(order.getOrderParts().stream()
                                .map(op -> new ItemDto(op.getCode(), op.getQuantity()))
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
    }

    public OrderResDto getOrder(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new NotFoundException(ErrorStatus.ORDER_NOT_FOUND.getMessage())
        );

        return OrderResDto.builder()
                .id(order.getId())
                .branch(order.getBranch())
                .status(order.getStatus())
                .items(order.getOrderParts().stream().map(
                        op -> new ItemDto(op.getCode(), op.getQuantity()))
                        .collect(Collectors.toList()))
                .build();
    }

    @Transactional
    public void updateOrderStatus(OrderStatusEvent orderStatusEvent) {
        Order order = orderRepository.findById(orderStatusEvent.getOrderId()).orElseThrow(
                () -> new NotFoundException(ErrorStatus.ORDER_NOT_FOUND.getMessage())
        );

        order.setStatus(orderStatusEvent.getOrderStatus());
        orderRepository.save(order);
    }

    @Transactional
    public void allocateWarehouse(OrderWarehouseEvent orderWarehouseEvent) {
        Order order = orderRepository.findById(orderWarehouseEvent.getOrderId()).orElseThrow(
                () -> new NotFoundException(ErrorStatus.ORDER_NOT_FOUND.getMessage())
        );

        order.setWarehouseId(orderWarehouseEvent.getWarehouseId());
        order.setWarehouseName(orderWarehouseEvent.getWarehouseName());
        orderRepository.save(order);
    }

}
