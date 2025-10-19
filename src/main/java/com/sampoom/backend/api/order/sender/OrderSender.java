package com.sampoom.backend.api.order.sender;

import com.sampoom.backend.api.order.dto.ToWarehouseDto;
import com.sampoom.backend.api.order.entity.Order;

public interface OrderSender {
    void sendOrderToWarehouse(ToWarehouseDto order);
//    void sendOrderToFactory(ToFactoryDto order);
//    void sendOrderToMaterial(ToMaterialDto order);
}

