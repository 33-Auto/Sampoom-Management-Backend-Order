package com.sampoom.backend.api.order.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.sampoom.backend.common.exception.BadRequestException;
import com.sampoom.backend.common.response.ErrorStatus;
import lombok.Getter;

@Getter
public enum Requester {
    FACTORY("공장"),
    WAREHOUSE("창고"),
    AGENCY("대리점");

    private final String korean;
    Requester(String korean) {this.korean = korean;}

    @JsonCreator
    public static Requester fromKorean(String koreanName) {
        for (Requester type : values()) {
            if (type.korean.equals(koreanName)) {
                return type;
            }
        }
        throw new BadRequestException(ErrorStatus.INVALID_REQUESTER.getMessage() + koreanName);
    }
}
