package com.sampoom.backend.api.order.repository;

import com.sampoom.backend.api.order.entity.Order;
import com.sampoom.backend.api.order.entity.Requester;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByRequester(Requester requester);
}
