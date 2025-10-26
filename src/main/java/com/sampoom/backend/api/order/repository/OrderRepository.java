package com.sampoom.backend.api.order.repository;

import com.sampoom.backend.api.order.entity.Order;
import com.sampoom.backend.api.order.entity.Requester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("""
            SELECT DISTINCT o
            FROM Order o
            LEFT JOIN FETCH o.orderParts
            WHERE o.requester = :requester
                AND (:branch IS NULL OR o.branch = :branch)
            """)
    List<Order> findWithItemsByRequesterAndBranch(@Param("requester") Requester requester, @Param("branch") String branch);
}
