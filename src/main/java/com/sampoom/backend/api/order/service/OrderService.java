package com.sampoom.backend.api.order.service;

import com.sampoom.backend.api.order.dto.OrderReqDto;
import com.sampoom.backend.api.order.dto.OrderResDto;
import com.sampoom.backend.api.order.dto.ToWarehouseDto;
import com.sampoom.backend.api.order.entity.Order;
import com.sampoom.backend.api.order.entity.OrderStatus;
import com.sampoom.backend.api.order.entity.Requester;
import com.sampoom.backend.api.order.repository.OrderRepository;
import com.sampoom.backend.api.order.sender.OrderSender;
import com.sampoom.backend.api.order.sender.WarehouseClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderPartService orderPartService;
    private final OrderMaterialService orderMaterialService;
    private final OrderSender orderSender;
    private final WarehouseClient warehouseClient;

    public OrderResDto createOrder(@RequestBody OrderReqDto orderReqDto) {
        Order newOrder = Order.builder()
                .requester(orderReqDto.getRequester())
                .branch(orderReqDto.getBranch())
                .type(orderReqDto.getType())
                .status(OrderStatus.PENDING)
                .build();
        orderRepository.save(newOrder);

        if (orderReqDto.getRequester() == Requester.FACTORY) {
            orderMaterialService.saveAllMaterials(newOrder.getId(), orderReqDto.getItems());
        }
        else {
            orderPartService.saveAllParts(newOrder.getId(), orderReqDto.getItems());

            if (orderReqDto.getRequester() == Requester.AGENCY) {
                ToWarehouseDto toWarehouseDto = ToWarehouseDto.builder()
                        .branch(orderReqDto.getBranch())
                        .items(orderReqDto.getItems())
                        .build();
                orderSender.sendOrderToWarehouse(toWarehouseDto);
            }
        }

        return OrderResDto.builder()
                .id(newOrder.getId())
                .requester(orderReqDto.getRequester())
                .branch(orderReqDto.getBranch())
                .type(orderReqDto.getType())
                .status(OrderStatus.PENDING)
                .items(orderReqDto.getItems())
                .build();
    }



}
