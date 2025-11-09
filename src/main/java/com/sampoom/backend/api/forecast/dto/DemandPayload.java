package com.sampoom.backend.api.forecast.dto;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DemandPayload {
    private UUID eventId;
    private String eventType;
    private OffsetDateTime occurredAt;
    private Integer version;
    private PartPayload payload;
}
