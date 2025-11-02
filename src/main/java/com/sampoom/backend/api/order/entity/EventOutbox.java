package com.sampoom.backend.api.order.entity;

import com.sampoom.backend.api.order.event.ToWarehouseEvent;
import com.sampoom.backend.common.entitiy.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "event_outbox")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventOutbox extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String topic;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", nullable = false)
    private String payload; // 이벤트 객체 (DB에 JSONB로 저장)

    @Column(nullable = false)
    @Builder.Default
    private EventStatus eventStatus = EventStatus.PENDING;

    public void markAsPublished() {
        this.eventStatus = EventStatus.PUBLISHED;
    }

    public void markAsFailed() {
        this.eventStatus = EventStatus.FAILED;
    }
}
