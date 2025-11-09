package com.sampoom.backend.api.forecast.controller;

import com.sampoom.backend.api.forecast.service.ForecastService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/forecast")
@RequiredArgsConstructor
public class ForecastController {
    private final ForecastService forecastService;

    @PostMapping
    public void startForecasting() {
        forecastService.scheduledForecasting();
    }
}
