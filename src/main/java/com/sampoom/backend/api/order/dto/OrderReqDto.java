package com.sampoom.backend.api.order.dto;

import jakarta.validation.constraints.NotEmpty;
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

    @NotNull(message = "Agency 지점명은 필수입니다.")
    private String agencyName;

    @NotEmpty(message = "주문 항목은 최소 1개입니다.")
    private List<ItemCategoryDto> items;
}
