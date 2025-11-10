package com.sampoom.backend.api.order.event;

import com.sampoom.backend.api.order.dto.ItemBriefDto;
import com.sampoom.backend.api.order.dto.ItemPartDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ToWarehouseEvent {
    private Long orderId;
    private Long agencyId;
    private String agencyName;
    private List<ItemBriefDto> items;
    private Long version;
    private OffsetDateTime sourceUpdatedAt;
}