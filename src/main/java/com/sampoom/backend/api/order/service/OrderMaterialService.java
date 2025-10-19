package com.sampoom.backend.api.order.service;

import com.sampoom.backend.api.order.dto.ItemDto;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderMaterialService {
    @Autowired
    EntityManager entityManager;

    @Transactional
    protected void saveAllMaterials(Long orderId, List<ItemDto> items) {
        if (items.isEmpty()) return;

        String sql = """
        INSERT INTO order_material (order_id, code, quantity)
        VALUES (?, ?, ?)
        """;

        var query = entityManager.createNativeQuery(sql);

        for (int i = 0; i < items.size(); i++) {
            ItemDto item = items.get(i);
            query.setParameter(1, orderId);
            query.setParameter(2, item.getCode());
            query.setParameter(3, item.getQuantity());
            query.executeUpdate();

            // 일정 단위로 flush/clear
            if (i % 100 == 0) {
                entityManager.flush();
                entityManager.clear();
            }
        }
    }
}
