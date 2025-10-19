package com.sampoom.backend.api.order.sender;

import com.sampoom.backend.api.order.dto.ToFactoryDto;
import com.sampoom.backend.api.order.dto.ToWarehouseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RestOrderSender implements OrderSender {
    private final WarehouseClient warehouseClient;
    private final FactoryClient factoryClient;

    @Override
    public void sendOrderToWarehouse(ToWarehouseDto order) {
        warehouseClient.sendOrderToWarehouse(order);
    }

    @Override
    public void sendOrderToFactory(ToFactoryDto order) {
        factoryClient.sendOrderToFactory(order);
    }
}

