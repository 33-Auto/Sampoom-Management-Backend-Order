package com.sampoom.backend.api.order.repository;

import com.sampoom.backend.api.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("""
            SELECT DISTINCT o
            FROM Order o
            LEFT JOIN FETCH o.orderParts
            WHERE (:branch IS NULL OR o.branch = :branch)
            """)
    Page<Order> findWithItemsByBranch(@Param("branch") String branch, Pageable pageable);
}
