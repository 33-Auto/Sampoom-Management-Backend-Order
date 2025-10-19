package com.sampoom.backend.api.order.dto;

import lombok.*;

@Getter @Setter
@ToString @Builder
@AllArgsConstructor
public class ItemDto {
    private String code;
    private Integer quantity;
}
