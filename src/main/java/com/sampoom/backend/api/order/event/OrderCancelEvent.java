package com.sampoom.backend.api.order.event;

import com.sampoom.backend.api.order.dto.ItemBriefDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderCancelEvent {
    private Long orderId;
    private Long warehouseId;
    List<ItemBriefDto> parts;
}
