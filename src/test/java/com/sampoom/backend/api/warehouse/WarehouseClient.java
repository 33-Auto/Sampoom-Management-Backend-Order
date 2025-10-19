package com.sampoom.backend.api.warehouse;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "warehouse-service", url = "${warehouse.url}")
public interface WarehouseClient {
    @PostMapping("/api/inventory/reserve")
    boolean reserveStock(@RequestParam("partId") Long partId,
                         @RequestParam("quantity") int quantity);
}
