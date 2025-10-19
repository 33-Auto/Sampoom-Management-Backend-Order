package com.sampoom.backend.api.order.sender;

import com.sampoom.backend.api.order.dto.ToWarehouseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RestOrderSender implements OrderSender {

    private final WarehouseClient warehouseClient;

    @Override
    public void sendOrderToWarehouse(ToWarehouseDto order) {
        warehouseClient.sendOrderToWarehouse(order);
    }
}

