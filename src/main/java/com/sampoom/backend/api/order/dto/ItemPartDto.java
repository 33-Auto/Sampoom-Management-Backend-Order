package com.sampoom.backend.api.order.dto;

import lombok.*;

@Getter @Setter
@ToString @Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemPartDto {
    private Long partId;
    private String name;
    private String code;
    private Integer quantity;
    private Integer standardCost;
}
