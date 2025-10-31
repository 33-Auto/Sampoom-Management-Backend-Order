package com.sampoom.backend.api.order.dto;

import com.sampoom.backend.api.order.entity.OrderStatus;
import com.sampoom.backend.api.order.entity.Requester;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter @Setter
@ToString
@Builder
public class OrderResDto {
    private Long id;
    private String branch;
    private List<ItemDto> items;
    private OrderStatus status;
}
