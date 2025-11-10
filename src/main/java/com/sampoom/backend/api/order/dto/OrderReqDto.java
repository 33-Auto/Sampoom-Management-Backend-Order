package com.sampoom.backend.api.order.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter @Setter
@ToString
@Builder
public class OrderReqDto {
    @NotNull(message = "Agency ID는 필수입니다.")
    private Long agencyId;
    private String agencyName;
    private List<ItemCategoryDto> items;
}
