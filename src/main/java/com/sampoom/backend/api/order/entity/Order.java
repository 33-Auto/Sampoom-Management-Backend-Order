package com.sampoom.backend.api.order.entity;

import com.sampoom.backend.common.entitiy.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order_table")
public class Order extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Requester requester;
    private String branch;
    private OrderStatus status;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    private List<OrderPart> orderParts = new ArrayList<>();
}