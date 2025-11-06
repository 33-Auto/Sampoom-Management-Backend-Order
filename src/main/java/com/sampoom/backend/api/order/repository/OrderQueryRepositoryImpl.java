package com.sampoom.backend.api.order.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sampoom.backend.api.order.dto.OrderWithStockDto;
import com.sampoom.backend.api.order.dto.OutboundFilterDto;
import com.sampoom.backend.api.order.dto.PartStockDto;
import com.sampoom.backend.api.order.entity.QOrder;
import com.sampoom.backend.api.order.entity.QOrderPart;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepositoryImpl implements OrderQueryRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<OrderWithStockDto> outboundSearch(OutboundFilterDto req, Pageable pageable) {
        QOrder order = QOrder.order;
        QOrderPart part = QOrderPart.orderPart;

        BooleanBuilder condition = new BooleanBuilder();

        if (req.getWarehouseId() != null)
            condition.and(order.warehouseId.eq(req.getWarehouseId()));
        if (req.getOrderStatus() != null)
            condition.and(order.status.eq(req.getOrderStatus()));
        if (req.getCategoryId() != null)
            condition.and(part.categoryId.eq(req.getCategoryId()));
        if (req.getGroupId() != null)
            condition.and(part.groupId.eq(req.getGroupId()));
        if (req.getKeyword() != null && !req.getKeyword().isEmpty())
            condition.and(order.branch.contains(req.getKeyword())
                    .or(order.orderNumber.contains(req.getKeyword())));

        List<Tuple> tuples = queryFactory
                .select(
                        order.id,
                        order.orderNumber,
                        order.branch.as("agencyName"),
                        order.status,
                        order.createdAt,
                        part.categoryName,
                        part.groupName,
                        part.partId,
                        part.name,
                        part.code,
                        part.quantity.as("orderQuantity"),
                        part.standardCost
                )
                .from(order)
                .leftJoin(order.orderParts, part)
                .where(condition)
                .orderBy(order.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Map<Long, OrderWithStockDto> grouped = new LinkedHashMap<>();

        for (Tuple tuple : tuples) {
            Long orderId = tuple.get(order.id);

            OrderWithStockDto dto = grouped.computeIfAbsent(orderId, id -> OrderWithStockDto.builder()
                    .orderId(tuple.get(order.id))
                    .orderNumber(tuple.get(order.orderNumber))
                    .agencyName(tuple.get(order.branch.as("agencyName")))
                    .status(tuple.get(order.status))
                    .createdAt(tuple.get(order.createdAt))
                    .items(new ArrayList<>())
                    .build()
            );

            if (tuple.get(part.partId) != null) {
                dto.getItems().add(PartStockDto.builder()
                        .categoryName(tuple.get(part.categoryName))
                        .groupName(tuple.get(part.groupName))
                        .partId(tuple.get(part.partId))
                        .name(tuple.get(part.name))
                        .code(tuple.get(part.code))
                        .orderQuantity(tuple.get(part.quantity.as("orderQuantity")))
                        .standardCost(tuple.get(part.standardCost))
                        .build());
            }
        }

        List<OrderWithStockDto> results = new ArrayList<>(grouped.values());

        long total = Optional.ofNullable(
                queryFactory
                        .select(order.countDistinct())
                        .from(order)
                        .leftJoin(order.orderParts, part)
                        .where(condition)
                        .fetchOne()
        ).orElse(0L);

        return new PageImpl<>(results, pageable, total);
    }
}
