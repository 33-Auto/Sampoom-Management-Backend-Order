package com.sampoom.backend.api.part.dto;

import com.sampoom.backend.api.part.entity.BomComplexity;
import com.sampoom.backend.common.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BomPayload {
    private Long partId;
    private String partCode;
    private String partName;
    private BomComplexity complexity;
    private Status status;
    private Boolean deleted;
}
