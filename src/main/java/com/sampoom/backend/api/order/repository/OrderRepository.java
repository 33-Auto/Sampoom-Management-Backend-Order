package com.sampoom.backend.api.order.repository;

import com.sampoom.backend.api.order.entity.Order;
import com.sampoom.backend.api.order.entity.Requester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByRequester(Requester requester);

    @Query("SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.orderParts WHERE o.requester = :requester")
    List<Order> findWithItemsByRequester(@Param("requester") Requester requester);
}
