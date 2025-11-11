package com.sampoom.backend.api.forecast.entity;

import com.sampoom.backend.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Forecast extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "warehouse_id", nullable = false)
    private Long warehouseId;

    @Column(name = "part_id", nullable = false)
    private Long partId;
    @Column(name = "part_code", nullable = false)
    private String partCode;
    @Column(name = "part_name", nullable = false)
    private String partName;

    @Column(nullable = false)
    private LocalDateTime month;

    @Column(name = "monthly_quantity", nullable = false)
    private Integer monthlyQuantity;
}
