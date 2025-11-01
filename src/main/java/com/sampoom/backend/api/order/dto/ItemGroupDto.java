package com.sampoom.backend.api.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemGroupDto {
    private Long groupId;
    private String groupName;
    private List<ItemPartDto> parts;
}
