package com.sampoom.backend.api.order.service;

import com.sampoom.backend.api.order.dto.ItemCategoryDto;
import com.sampoom.backend.api.order.dto.ItemGroupDto;
import com.sampoom.backend.api.order.dto.ItemPartDto;
import com.sampoom.backend.common.exception.BadRequestException;
import com.sampoom.backend.common.response.ErrorStatus;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderPartService {
    private final EntityManager entityManager;

    @Transactional
    protected void saveAllParts(Long orderId, List<ItemCategoryDto> items) {
        if (items.isEmpty()) throw new BadRequestException(ErrorStatus.NO_CATEGORY_REQUEST.getMessage());

        String sql = """
        INSERT INTO order_part (order_id, category_id, category_name, group_id, group_name, part_id, code, name, quantity, standard_cost)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        var query = entityManager.createNativeQuery(sql);

        for (int i = 0; i < items.size(); i++) {
            ItemCategoryDto itemCategory = items.get(i);

            List<ItemGroupDto> groups = itemCategory.getGroups();
            if (groups.isEmpty()) {
                throw new BadRequestException(ErrorStatus.NO_GROUP_REQUEST.getMessage());
            }

            for (ItemGroupDto itemGroup : groups) {
                List<ItemPartDto> parts = itemGroup.getParts();
                if (parts.isEmpty()) {
                    throw new BadRequestException(ErrorStatus.NO_PART_REQUEST.getMessage());
                }

                for (ItemPartDto itemPart : parts) {
                    query.setParameter(1, orderId);
                    query.setParameter(2, itemCategory.getCategoryId());
                    query.setParameter(3, itemCategory.getCategoryName());
                    query.setParameter(4, itemGroup.getGroupId());
                    query.setParameter(5, itemGroup.getGroupName());
                    query.setParameter(6, itemPart.getPartId());
                    query.setParameter(7, itemPart.getCode());
                    query.setParameter(8, itemPart.getName());
                    query.setParameter(9, itemPart.getQuantity());
                    query.setParameter(10, itemPart.getStandardCost());
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
