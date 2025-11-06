package com.sampoom.backend.api.order.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.sampoom.backend.api.order.entity.OrderStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderWithStockDto {
    private Long orderId;
    private String orderNumber;
    private String agencyName;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private List<PartStockDto> items;

    @QueryProjection
    public OrderWithStockDto (Long orderId, String orderNumber, String agencyName, OrderStatus status, LocalDateTime createdAt) {
        this.orderId = orderId;
        this.orderNumber = orderNumber;
        this.agencyName = agencyName;
        this.status = status;
        this.createdAt = createdAt;
    }
}
