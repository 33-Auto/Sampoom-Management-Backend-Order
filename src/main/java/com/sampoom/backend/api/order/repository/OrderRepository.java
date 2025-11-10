package com.sampoom.backend.api.order.repository;

import com.sampoom.backend.api.order.entity.Order;
import com.sampoom.backend.api.order.entity.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, OrderQueryRepository {
    @Query("""
            SELECT o
            FROM Order o
            WHERE (:branch IS NULL OR o.agencyName = :agencyName)
            """)
    Page<Order> findWithItemsByAgencyName(@Param("agencyName") String agencyName, Pageable pageable);

    @Query("""
        SELECT o
        FROM Order o
        WHERE o.warehouseId = :warehouseId
        AND (:branch IS NULL OR o.agencyName = :agencyName)
        AND (:status IS NULL OR o.status = :status)
    """)
    Page<Order> findOrdersForWarehouse(
            @Param("warehouseId") Long warehouseId,
            @Param("agencyName") String agencyName,
            @Param("status") OrderStatus status,
            Pageable pageable
    );
}
