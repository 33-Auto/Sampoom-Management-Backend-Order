package com.sampoom.backend.api.order.sender;

import com.sampoom.backend.api.order.dto.ToWarehouseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class WarehouseClient {

    private final WebClient webClient;

    public void sendOrderToWarehouse(ToWarehouseDto orderDto) {
        webClient.patch()
                .uri("http://warehouse-service:8080/order")
                .bodyValue(orderDto)
                .retrieve()
                .bodyToMono(Void.class)
                .block(); // block()은 동기 호출용, 나중에 비동기로 바꿀 수도 있음
    }
}

