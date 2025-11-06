package com.sampoom.backend.api.client.warehouse;

import com.sampoom.backend.api.order.dto.PartStockResDto;
import com.sampoom.backend.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WarehouseClient {
    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://sampoom.store/api/warehouse")
            .build();

    public List<PartStockResDto> getCurrentStocks(Long warehouseId, List<Long> partIds) {
        String partIdsParam = partIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        return Objects.requireNonNull(webClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .path("/order")
                                .queryParam("warehouseId", warehouseId)
                                .queryParam("partIds", partIdsParam)
                                .build())
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<ApiResponse<List<PartStockResDto>>>() {
                        })
                        .block())
                .getData();
    }
}