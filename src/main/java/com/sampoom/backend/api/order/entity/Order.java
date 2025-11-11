package com.sampoom.backend.api.order.entity;

import com.sampoom.backend.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order_table")
public class Order extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String orderNumber;

    @Column(name = "agency_id", nullable = false)
    private Long agencyId;

    @Column(nullable = false)
    private String agencyName;

    private Long warehouseId;
    private String warehouseName;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    @BatchSize(size = 50)
    @Builder.Default
    private List<OrderPart> orderParts = new ArrayList<>();

    @Version
    private Long version; // JPA가 자동 관리 (낙관적 락 + 자동 증가)
}