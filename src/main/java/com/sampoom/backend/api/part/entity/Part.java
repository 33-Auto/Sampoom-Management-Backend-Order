package com.sampoom.backend.api.part.entity;

import com.sampoom.backend.common.entitiy.Status;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Part {
    @Id
    private Long id;

    @Column(unique = true, nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BomComplexity complexity;

    @Column(nullable = false)
    private Status status;

    @Column(nullable = false)
    private boolean deleted;
}
