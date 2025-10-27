package com.sampoom.backend.api.order.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sampoom.backend.api.order.entity.EventOutbox;
import com.sampoom.backend.api.order.entity.EventStatus;
import com.sampoom.backend.api.order.entity.OrderStatus;
import com.sampoom.backend.api.order.repository.EventOutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class EventPublisher {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final EventOutboxRepository eventOutboxRepository;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedDelay = 2000)
    public void publishPendingEvents() {
        List<EventOutbox> pendingEvents = eventOutboxRepository.findByEventStatus(EventStatus.PENDING);
        List<EventOutbox> retryEvents = eventOutboxRepository.findByEventStatus(EventStatus.FAILED);
        pendingEvents.addAll(retryEvents);

        for (EventOutbox event : pendingEvents) {
            try {
                kafkaTemplate.send(event.getTopic(), objectMapper.writeValueAsString(event.getPayload()))
                        .thenAccept(result -> {
                            event.setEventStatus(EventStatus.PUBLISHED);
                            eventOutboxRepository.save(event);
                            log.info("✅ Sent outbox event: {}", event.getId());
                        })
                        .exceptionally(ex -> {
                            event.setEventStatus(EventStatus.FAILED);
                            eventOutboxRepository.save(event);
                            log.error("❌ Failed to send outbox event: {}", event.getId(), ex);
                            return null;
                        });
            } catch (Exception e) {
                event.setEventStatus(EventStatus.FAILED);
                eventOutboxRepository.save(event);
                log.error("❌ Failed to serialize event: {}", event.getId(), e);
            }
        }
    }
}
