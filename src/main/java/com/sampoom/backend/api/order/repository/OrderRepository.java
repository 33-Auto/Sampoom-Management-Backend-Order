package com.sampoom.backend.api.order.repository;

import com.sampoom.backend.api.order.entity.Order;
import com.sampoom.backend.api.order.entity.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("""
            SELECT o
            FROM Order o
            WHERE (:branch IS NULL OR o.branch = :branch)
            """)
    Page<Order> findWithItemsByBranch(@Param("branch") String branch, Pageable pageable);

    @Query("""
        SELECT o
        FROM Order o
        WHERE o.warehouseId = :warehouseId
        AND (:branch IS NULL OR o.branch = :branch)
        AND (:status IS NULL OR o.status = :status)
        ORDER BY o.createdAt DESC
    """)
    Page<Order> findOrdersForWarehouse(
            @Param("warehouseId") Long warehouseId,
            @Param("branch") String branch,
            @Param("status") OrderStatus status,
            Pageable pageable
    );
}
