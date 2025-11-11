package com.sampoom.backend.api.forecast.service;

import com.sampoom.backend.api.event.service.EventService;
import com.sampoom.backend.api.forecast.dto.DemandPayload;
import com.sampoom.backend.api.forecast.dto.PartPayload;
import com.sampoom.backend.api.forecast.dto.SixMonthDemandDto;
import com.sampoom.backend.api.forecast.entity.Forecast;
import com.sampoom.backend.api.forecast.repository.ForecastRepository;
import com.sampoom.backend.api.event.entity.EventOutbox;
import com.sampoom.backend.api.order.repository.EventOutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ForecastService {
    private final ForecastRepository forecastRepository;
    private final EventOutboxRepository eventOutboxRepository;
    private final EventService eventService;

//    @Scheduled(cron = "0 0 3 1 * *") // 매월 1일 새벽 3 시에 실행
    @Transactional
    public void scheduledForecasting() {
        LocalDateTime targetMonth = LocalDateTime.now().plusMonths(3);
        log.info("🌙 [ForecastScheduler] Running monthly forecast for {}", targetMonth.getMonth());

        LocalDateTime sixMonthsAgo = LocalDateTime.now().minusMonths(6);
        List<SixMonthDemandDto> sixMonthDataByPart = forecastRepository.findMonthlyComplexPartDemand(sixMonthsAgo);
        log.info("sixMonthDataByPart = {}", sixMonthDataByPart);
        List<Forecast> forecasts = new ArrayList<>();
        List<EventOutbox> outboxEvents = new ArrayList<>();

        for (SixMonthDemandDto dto : sixMonthDataByPart) {
            int forecastedQuantity = Math.toIntExact(dto.getSumQuantity() / 6);

            forecasts.add(Forecast.builder()
                    .warehouseId(dto.getWarehouseId())
                    .month(targetMonth)
                    .partId(dto.getPartId())
                    .partCode(dto.getPartCode())
                    .partName(dto.getPartName())
                    .monthlyQuantity(forecastedQuantity)
                    .build());

            PartPayload partPayload = PartPayload.builder()
                    .warehouseId(dto.getWarehouseId())
                    .partId(dto.getPartId())
                    .demandMonth(targetMonth)
                    .demandQuantity(forecastedQuantity)
                    .build();

            DemandPayload demandPayload = DemandPayload.builder()
                    .eventId(UUID.randomUUID())
                    .eventType("PartForecast")
                    .occurredAt(OffsetDateTime.now(ZoneOffset.of("+09:00")))
                    .version(0)
                    .payload(partPayload)
                    .build();

            outboxEvents.add(EventOutbox.builder()
                    .payload(eventService.serializePayload(demandPayload))
                    .topic("part-forecast")
                    .build());
        }

        forecastRepository.saveAll(forecasts);
        eventOutboxRepository.saveAll(outboxEvents);
    }
}
