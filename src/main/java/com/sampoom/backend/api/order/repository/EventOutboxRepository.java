package com.sampoom.backend.api.order.repository;

import com.sampoom.backend.api.order.entity.EventOutbox;
import com.sampoom.backend.api.order.entity.EventStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventOutboxRepository extends JpaRepository<EventOutbox, Long> {
    List<EventOutbox> findByEventStatus(EventStatus status);
}
