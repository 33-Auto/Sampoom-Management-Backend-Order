package com.sampoom.backend.api.order.dto;

import com.sampoom.backend.api.order.entity.OrderStatus;
import lombok.Getter;

@Getter
public class OrderStatusEvent {
    private Long  orderId;
    private OrderStatus orderStatus;
}
