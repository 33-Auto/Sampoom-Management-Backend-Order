package com.sampoom.backend.api.order.sender;

import com.sampoom.backend.api.order.dto.ToFactoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class FactoryClient {

    private final WebClient webClient;

    public void sendOrderToFactory(ToFactoryDto orderDto) {
        webClient.patch()
                .uri("http://factory-service/order")
                .bodyValue(orderDto)
                .retrieve()
                .bodyToMono(Void.class)
                .block(); // block()은 동기 호출용, 나중에 비동기로 바꿀 수도 있음
    }
}

