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
    private LocalDateTime month;

    public SixMonthDemandDto(Long warehouseId,
                             Long partId,
                             String partCode,
                             String partName,
                             Long sumQuantity,
                             Object month) {
        this.warehouseId = warehouseId;
        this.partId = partId;
        this.partCode = partCode;
        this.partName = partName;
        this.sumQuantity = sumQuantity;

        if (month instanceof java.sql.Timestamp ts) {
            this.month = ts.toLocalDateTime();
        } else if (month instanceof LocalDateTime lt) {
            this.month = lt;
        } else {
            this.month = null;
        }    }
}