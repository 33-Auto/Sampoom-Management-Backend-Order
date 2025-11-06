package com.sampoom.backend.api.order.dto;

import com.sampoom.backend.api.order.entity.OrderStatus;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OutboundFilterDto {
    private Long warehouseId;
    private Long categoryId;
    private Long groupId;
    private String keyword;
    private OrderStatus orderStatus;
}
