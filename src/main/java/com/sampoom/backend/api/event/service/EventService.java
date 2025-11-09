package com.sampoom.backend.api.event.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sampoom.backend.api.event.entity.EventOutbox;
import com.sampoom.backend.api.event.entity.EventStatus;
import com.sampoom.backend.api.order.repository.EventOutboxRepository;
import com.sampoom.backend.common.exception.BadRequestException;
import com.sampoom.backend.common.response.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventOutboxRepository eventOutboxRepository;
    private final ObjectMapper objectMapper;

    public void setEventOutBox(String topic, Object payload) {
        EventOutbox eventOutbox = EventOutbox.builder()
                .topic(topic)
                .payload(this.serializePayload(payload))
                .eventStatus(EventStatus.PENDING)
                .build();
        eventOutboxRepository.save(eventOutbox);
    }

    public String serializePayload(Object payload) {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            throw new BadRequestException(ErrorStatus.FAIL_SERIALIZE.getMessage());
        }
    }
}
