package com.sampoom.backend.api.order.dto;

import com.sampoom.backend.api.order.entity.OrderType;
import com.sampoom.backend.api.order.entity.Requester;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter @Setter
@ToString
public class OrderReqDto {
    private Requester requester;
    private String branch;
    private OrderType type;
    private List<ItemDto> items;
}
