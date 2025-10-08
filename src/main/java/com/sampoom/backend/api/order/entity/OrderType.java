package com.sampoom.backend.api.order.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.sampoom.backend.common.exception.BadRequestException;
import com.sampoom.backend.common.response.ErrorStatus;
import lombok.Getter;

@Getter
public enum OrderType {
    MATERIAL("자재"),
    PART("부품");

    private final String korean;
    OrderType(String korean) {
        this.korean = korean;
    }

    @JsonCreator
    public static OrderType fromKorean(String koreanName) {
        for (OrderType type : values()) {
            if (type.korean.equals(koreanName)) {
                return type;
            }
        }
        throw new BadRequestException(ErrorStatus.INVALID_ORDER_TYPE.getMessage() + koreanName);
    }
}
