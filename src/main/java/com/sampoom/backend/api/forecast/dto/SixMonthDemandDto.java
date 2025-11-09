package com.sampoom.backend.api.forecast.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class SixMonthDemandDto {
    private Long warehouseId;
    private Long partId;
    private String partCode;
    private String partName;
    private Long sumQuantity;

    public SixMonthDemandDto(Long warehouseId,
                             Long partId,
                             String partCode,
                             String partName,
                             Long sumQuantity) {
        this.warehouseId = warehouseId;
        this.partId = partId;
        this.partCode = partCode;
        this.partName = partName;
        this.sumQuantity = sumQuantity;
    }
}