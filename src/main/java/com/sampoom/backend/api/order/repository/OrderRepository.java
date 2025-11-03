package com.sampoom.backend.api.order.repository;

import com.sampoom.backend.api.order.entity.Order;
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
            WHERE (:warehouseId IS NULL OR o.warehouseId = :warehouseId)
            """)
    Page<Order> findWithItemsByWarehouseId(@Param("warehouseId") Long warehouseId, Pageable pageable);
}
