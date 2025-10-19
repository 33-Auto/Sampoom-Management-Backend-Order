package com.sampoom.backend.api.order.service;

import com.sampoom.backend.api.order.dto.OrderReqDto;
import com.sampoom.backend.api.order.dto.OrderResDto;
import com.sampoom.backend.api.order.dto.ToFactoryDto;
import com.sampoom.backend.api.order.dto.ToWarehouseDto;
import com.sampoom.backend.api.order.entity.Order;
import com.sampoom.backend.api.order.entity.OrderStatus;
import com.sampoom.backend.api.order.entity.Requester;
import com.sampoom.backend.api.order.repository.OrderRepository;
import com.sampoom.backend.api.order.sender.OrderSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderPartService orderPartService;
    private final OrderSender orderSender;

    public OrderResDto createOrder(OrderReqDto orderReqDto) {
        Order newOrder = Order.builder()
                .requester(orderReqDto.getRequester())
                .branch(orderReqDto.getBranch())
                .status(OrderStatus.PENDING)
                .build();

        orderRepository.save(newOrder);
        orderPartService.saveAllParts(newOrder.getId(), orderReqDto.getItems());

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

        return OrderResDto.builder()
                .id(newOrder.getId())
                .requester(orderReqDto.getRequester())
                .branch(orderReqDto.getBranch())
                .status(OrderStatus.PENDING)
                .items(orderReqDto.getItems())
                .build();
    }



}
