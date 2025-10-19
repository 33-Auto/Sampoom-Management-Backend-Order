package com.sampoom.backend.api.order.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_part")
@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderPart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private String code;
    private Integer quantity;
}
