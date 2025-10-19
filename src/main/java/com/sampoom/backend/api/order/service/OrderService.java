package com.sampoom.backend.api.order.service;

import com.sampoom.backend.api.order.dto.*;
import com.sampoom.backend.api.order.entity.Order;
import com.sampoom.backend.api.order.entity.OrderStatus;
import com.sampoom.backend.api.order.entity.Requester;
import com.sampoom.backend.api.order.repository.OrderPartRepository;
import com.sampoom.backend.api.order.repository.OrderRepository;
import com.sampoom.backend.api.order.sender.OrderSender;
import com.sampoom.backend.common.exception.BadRequestException;
import com.sampoom.backend.common.response.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderPartRepository orderPartRepository;
    private final OrderPartService orderPartService;
    private final OrderSender orderSender;

    @Transactional
    public OrderResDto createOrder(OrderReqDto orderReqDto) {
        Order newOrder = Order.builder()
                .requester(orderReqDto.getRequester())
                .branch(orderReqDto.getBranch())
                .status(OrderStatus.PENDING)
                .build();

        orderRepository.save(newOrder);
        orderPartService.saveAllParts(newOrder.getId(), orderReqDto.getItems());

        return OrderResDto.builder()
                .id(newOrder.getId())
                .requester(orderReqDto.getRequester())
                .branch(orderReqDto.getBranch())
                .status(OrderStatus.PENDING)
                .items(orderReqDto.getItems())
                .build();
    }

    public void sendOrderToDownstream(OrderReqDto orderReqDto) {
        if (orderReqDto.getRequester() == Requester.AGENCY) {
            ToWarehouseDto toWarehouseDto = ToWarehouseDto.builder()
                    .branch(orderReqDto.getBranch())
                    .items(orderReqDto.getItems())
                    .build();
            orderSender.sendOrderToWarehouse(toWarehouseDto);
        } else if (orderReqDto.getRequester() == Requester.WAREHOUSE) {
            ToFactoryDto toFactoryDto = ToFactoryDto.builder()
                    .branch(orderReqDto.getBranch())
                    .items(orderReqDto.getItems())
                    .build();
            orderSender.sendOrderToFactory(toFactoryDto);
        }
    }

    public List<OrderResDto> getOrders(String from) {
        Requester requester;

        if (from == null)
            throw new BadRequestException(ErrorStatus.NO_QUERY_PARAMETER.getMessage());
        else if (from.equals(Requester.AGENCY.toString().toLowerCase())) {
            requester = Requester.AGENCY;
        } else if (from.equals(Requester.WAREHOUSE.toString().toLowerCase())) {
            requester = Requester.WAREHOUSE;
        } else
            throw new BadRequestException(ErrorStatus.INVALID_QUERY_PARAMETER.getMessage());

        List<Order> orders = orderRepository.findWithItemsByRequester(requester);

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
