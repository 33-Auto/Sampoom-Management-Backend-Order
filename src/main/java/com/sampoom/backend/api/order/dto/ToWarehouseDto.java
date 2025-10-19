package com.sampoom.backend.api.order.dto;

import com.sampoom.backend.api.order.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ToWarehouseDto {
    private String branch;
    private List<ItemDto> items;
}
