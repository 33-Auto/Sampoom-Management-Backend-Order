package com.sampoom.backend.api.event.service;

import com.sampoom.backend.api.part.dto.BomPayload;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class EventPayloadMapper {

    private final Map<String, Class<?>> registry = new HashMap<>();

    public EventPayloadMapper() {
        registry.put("BomCreated", BomPayload.class);
        registry.put("BomUpdated", BomPayload.class);
    }

    public Class<?> getPayloadClass(String eventType) {
        return registry.get(eventType);
    }
}

