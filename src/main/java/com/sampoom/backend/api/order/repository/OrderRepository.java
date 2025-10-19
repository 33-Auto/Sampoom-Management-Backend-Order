package com.sampoom.backend.api.order.repository;

import com.sampoom.backend.api.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
