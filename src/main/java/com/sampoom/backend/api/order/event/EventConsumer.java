package com.sampoom.backend.api.order.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sampoom.backend.api.order.dto.OrderStatusEvent;
import com.sampoom.backend.api.order.dto.OrderWarehouseEvent;
import com.sampoom.backend.api.order.service.OrderService;
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
        } catch (Exception e) {
            log.error("❌ Failed to process Order status update event", e);
        }
    }

    @KafkaListener(topics = "order-warehouse-events")
    public void consumeWarehouseEvent(String message) {
        try {
            OrderWarehouseEvent orderWarehouseEvent = objectMapper.readValue(message, OrderWarehouseEvent.class);

            orderService.allocateWarehouse(orderWarehouseEvent);

            log.info("✅ Received OrderWarehouseEvent: orderId={}", orderWarehouseEvent.getOrderId());
        } catch (Exception e) {
            log.error("❌ Failed to process Order warehouse allocating event", e);
        }
    }
}
