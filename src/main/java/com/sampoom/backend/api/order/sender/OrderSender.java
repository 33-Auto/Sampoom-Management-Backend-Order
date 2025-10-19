package com.sampoom.backend.api.order.sender;

import com.sampoom.backend.api.order.dto.ToFactoryDto;
import com.sampoom.backend.api.order.dto.ToWarehouseDto;

public interface OrderSender {
    void sendOrderToWarehouse(ToWarehouseDto order);
    void sendOrderToFactory(ToFactoryDto order);
}

