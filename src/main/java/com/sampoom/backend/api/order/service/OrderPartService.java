package com.sampoom.backend.api.order.service;

import com.sampoom.backend.api.order.dto.ItemCategoryDto;
import com.sampoom.backend.api.order.dto.ItemGroupDto;
import com.sampoom.backend.api.order.dto.ItemPartDto;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderPartService {
    @Autowired
    EntityManager entityManager;

    protected void saveAllParts(Long orderId, List<ItemCategoryDto> items) {
        if (items.isEmpty()) return;

        String sql = """
        INSERT INTO order_part (order_id, category_id, category_name, group_id, group_name, part_id, code, name, quantity)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        var query = entityManager.createNativeQuery(sql);

        for (int i = 0; i < items.size(); i++) {
            ItemCategoryDto itemCategory = items.get(i);
            List<ItemGroupDto> groups = itemCategory.getGroups();

            for (int j = 0; j < groups.size(); j++) {
                ItemGroupDto itemGroup = groups.get(j);
                List<ItemPartDto> parts = itemGroup.getParts();

                for (int k = 0; k < parts.size(); k++) {
                    ItemPartDto itemPart = parts.get(k);

                    query.setParameter(1, orderId);
                    query.setParameter(2, itemCategory.getCategoryId());
                    query.setParameter(3, itemCategory.getCategoryName());
                    query.setParameter(4, itemGroup.getGroupId());
                    query.setParameter(5, itemGroup.getGroupName());
                    query.setParameter(6, itemPart.getPartId());
                    query.setParameter(7, itemPart.getCode());
                    query.setParameter(8, itemPart.getName());
                    query.setParameter(9, itemPart.getQuantity());
                    query.executeUpdate();
                }
            }

            // 일정 단위로 flush/clear
            if (i % 100 == 0) {
                entityManager.flush();
                entityManager.clear();
            }
        }
    }
}
