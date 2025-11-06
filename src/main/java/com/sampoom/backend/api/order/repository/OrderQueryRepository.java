package com.sampoom.backend.api.order.repository;

import com.sampoom.backend.api.order.dto.OrderWithStockDto;
import com.sampoom.backend.api.order.dto.OutboundFilterDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderQueryRepository {
    Page<OrderWithStockDto> outboundSearch(OutboundFilterDto outboundFilterDto, Pageable pageable);
}
