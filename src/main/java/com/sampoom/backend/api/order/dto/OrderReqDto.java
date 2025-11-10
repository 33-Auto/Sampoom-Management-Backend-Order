package com.sampoom.backend.api.order.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter @Setter
@ToString
@Builder
public class OrderReqDto {
    private Long agencyId;
    private String agencyName;
    private List<ItemCategoryDto> items;
}
