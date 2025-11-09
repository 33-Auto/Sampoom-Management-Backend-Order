package com.sampoom.backend.api.order.event;

import com.sampoom.backend.api.order.entity.EventOutbox;
import com.sampoom.backend.api.order.entity.EventStatus;
import com.sampoom.backend.api.order.repository.EventOutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private static final int BATCH_SIZE = 100;

    @Scheduled(fixedDelay = 2000)
    public void publishPendingEvents() {
        Pageable pageable = PageRequest.of(0, BATCH_SIZE);
        List<EventOutbox> pendingEvents = eventOutboxRepository.findByEventStatus(EventStatus.PENDING, pageable);
        List<EventOutbox> retryEvents = eventOutboxRepository.findByEventStatus(EventStatus.FAILED, pageable);
        pendingEvents.addAll(retryEvents);

        for (EventOutbox event : pendingEvents) {
            kafkaTemplate.send(event.getTopic(),event.getPayload())
                    .thenAccept(result -> {
                        event.markAsPublished();
                        eventOutboxRepository.save(event);
                        log.info("✅ Sent outbox event: {}", event.getId());
                    })
                    .exceptionally(ex -> {
                        event.markAsFailed();
                        eventOutboxRepository.save(event);
                        log.error("❌ Failed to send outbox event: {}", event.getId(), ex);
                        return null;
                    });
        }
    }
}
