package com.sampoom.backend.api.order.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sampoom.backend.api.order.dto.OrderStatusEvent;
import com.sampoom.backend.api.order.dto.OrderWarehouseEvent;
import com.sampoom.backend.api.order.service.OrderService;
import com.sampoom.backend.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventConsumer {
    private final ObjectMapper objectMapper;
    private final OrderService orderService;

    @KafkaListener(topics = "order-status-events")
    public void consumeOrderStatus(String message) {
        try {
            OrderStatusEvent orderStatusEvent = objectMapper.readValue(message, OrderStatusEvent.class);

            orderService.updateOrderStatus(orderStatusEvent);

            log.info("✅ Received OrderStatusEvent: orderId={}", orderStatusEvent.getOrderId());
        } catch (JsonProcessingException e) {
            log.error("❌ Failed to deserialize Order status event: {}", message, e);
            // 역직렬화 실패는 재시도해도 성공하지 않으므로 DLQ로 전송
        } catch (NotFoundException e) {
            log.error("❌ Order not found for status update", e);
            // 비즈니스 예외 처리
        } catch (Exception e) {
            log.error("❌ Failed to process Order status update event", e);
            throw e; // 일시적 오류는 재시도를 위해 예외를 다시 throw
        }
    }

    @KafkaListener(topics = "order-warehouse-events")
    public void consumeWarehouseEvent(String message) {
        try {
            OrderWarehouseEvent orderWarehouseEvent = objectMapper.readValue(message, OrderWarehouseEvent.class);

            orderService.allocateWarehouse(orderWarehouseEvent);

            log.info("✅ Received OrderWarehouseEvent: orderId={}", orderWarehouseEvent.getOrderId());
        } catch (JsonProcessingException e) {
            log.error("❌ Failed to deserialize Order warehouse event: {}", message, e);
            // 역직렬화 실패는 재시도해도 성공하지 않으므로 DLQ로 전송
        } catch (NotFoundException e) {
            log.error("❌ Order not found for warehouse update", e);
            // 비즈니스 예외 처리
        } catch (Exception e) {
            log.error("❌ Failed to process Order warehouse allocating event", e);
            throw e; // 일시적 오류는 재시도를 위해 예외를 다시 throw
        }
    }
}
