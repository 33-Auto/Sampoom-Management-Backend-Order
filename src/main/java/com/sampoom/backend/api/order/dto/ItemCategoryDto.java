package com.sampoom.backend.api.order.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemCategoryDto {
    private Long categoryId;
    private String categoryName;
    List<ItemGroupDto> groups;
}
