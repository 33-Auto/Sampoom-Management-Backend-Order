package com.sampoom.backend.api.forecast.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartPayload {
    private Long partId;
    private Long warehouseId;
    private Integer demandQuantity;
    private LocalDateTime demandMonth;
    private Integer stock;
}
