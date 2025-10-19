package com.sampoom.backend.api.order.dto;

import com.sampoom.backend.api.order.entity.OrderStatus;
import com.sampoom.backend.api.order.entity.OrderType;
import com.sampoom.backend.api.order.entity.Requester;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter @Setter
@ToString
@Builder
public class OrderResDto {
    private Long id;
    private Requester requester;
    private String branch;
    private OrderType type;
    private List<ItemDto> items;
    private OrderStatus status;
}
