package com.sampoom.backend.api.order.dto;

import com.sampoom.backend.api.order.entity.OrderStatus;
import lombok.*;

import java.util.List;

@Getter @Setter
@ToString
@Builder
public class OrderResDto {
    private Long orderId;
    private String orderNumber;
    private String agencyName;
    private OrderStatus status;
    private String createdAt;
    private List<ItemCategoryDto> items;
}
