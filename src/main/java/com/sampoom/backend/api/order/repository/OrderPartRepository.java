package com.sampoom.backend.api.order.repository;

import com.sampoom.backend.api.order.dto.ItemDto;
import com.sampoom.backend.api.order.entity.OrderPart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderPartRepository extends JpaRepository<OrderPart, Long> {
    @Query("SELECT new com.sampoom.backend.api.order.dto.ItemDto(op.code, op.quantity) " +
            "FROM OrderPart op WHERE op.order.id = :orderId")
    List<ItemDto> findByOrderId(@Param("orderId") Long orderId);
}
