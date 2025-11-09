package com.sampoom.backend.api.order.entity;

import com.sampoom.backend.common.entitiy.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_part")
@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderPart extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name = "category_id", nullable = false)
    private Long categoryId;
    @Column(name = "category_name", nullable = false)
    private String categoryName;
    @Column(name = "group_id", nullable = false)
    private Long groupId;
    @Column(name = "group_name", nullable = false)
    private String groupName;
    @Column(name = "part_id", nullable = false)
    private Long partId;
    @Column(nullable = false)
    private String code;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Integer quantity;
    @Column(name = "standard_cost", nullable = false)
    private Integer standardCost;
}
