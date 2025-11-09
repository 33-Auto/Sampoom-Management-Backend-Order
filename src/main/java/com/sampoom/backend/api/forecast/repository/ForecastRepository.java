package com.sampoom.backend.api.forecast.repository;

import com.sampoom.backend.api.forecast.dto.SixMonthDemandDto;
import com.sampoom.backend.api.forecast.entity.Forecast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ForecastRepository extends JpaRepository<Forecast, Long> {
    @Query("""
        SELECT new com.sampoom.backend.api.forecast.dto.SixMonthDemandDto(
            o.warehouseId,
            p.id,
            p.code,
            p.name,
            SUM(op.quantity),
            function('date_trunc', 'month', o.createdAt)
        )
        FROM OrderPart op
        JOIN op.order o
        JOIN Part p ON op.partId = p.id
        WHERE p.complexity = 'COMPLEX'
            AND o.createdAt >= :sixMonthsAgo
        GROUP BY o.warehouseId, p.id, p.code, p.name, function('date_trunc', 'month', o.createdAt)
        ORDER BY o.warehouseId, function('date_trunc', 'month', o.createdAt)
    """)
    List<SixMonthDemandDto> findMonthlyComplexPartDemand(@Param("sixMonthsAgo") LocalDateTime sixMonthsAgo);
}
