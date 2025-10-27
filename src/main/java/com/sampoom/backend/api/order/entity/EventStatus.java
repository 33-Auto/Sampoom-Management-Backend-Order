package com.sampoom.backend.api.order.entity;

import lombok.Getter;

@Getter
public enum EventStatus {
    PENDING,
    PUBLISHED,
    FAILED
}
