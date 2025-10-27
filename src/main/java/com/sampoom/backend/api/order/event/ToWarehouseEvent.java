package com.sampoom.backend.api.order.event;

import com.sampoom.backend.api.order.dto.ItemDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ToWarehouseEvent {
    private Long orderId;
    private String branch;
    private List<ItemDto> items;
}