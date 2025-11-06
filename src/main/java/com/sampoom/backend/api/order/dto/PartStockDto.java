package com.sampoom.backend.api.order.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PartStockDto {
    private String categoryName;
    private String groupName;
    private Long partId;
    private String name;
    private String code;
    private Integer stock;
    private Integer orderQuantity;
    private Integer standardCost;

    @QueryProjection
    public PartStockDto(String categoryName, String groupName, Long partId, String name, String code, Integer orderQuantity, Integer standardCost) {
        this.categoryName = categoryName;
        this.groupName = groupName;
        this.partId = partId;
        this.name = name;
        this.code = code;
        this.orderQuantity = orderQuantity;
        this.standardCost = standardCost;
    }
}
