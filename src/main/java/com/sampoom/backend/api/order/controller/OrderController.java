package com.sampoom.backend.api.order.controller;

import com.sampoom.backend.api.order.dto.OrderReqDto;
import com.sampoom.backend.api.order.dto.OrderResDto;
import com.sampoom.backend.api.order.service.OrderService;
import com.sampoom.backend.common.response.ApiResponse;
import com.sampoom.backend.common.response.SuccessStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResDto>> createOrder(@RequestBody OrderReqDto order) {
        OrderResDto orderResDto = orderService.createOrder(order);

        return ApiResponse.success(SuccessStatus.CREATED, orderResDto);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderResDto>> getOrder(@PathVariable Long orderId) {
        return ApiResponse.success(SuccessStatus.OK, orderService.getOrder(orderId));
    }

    @PatchMapping("/cancel/{orderId}")
    public ResponseEntity<ApiResponse<Void>> cancelOrder(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
        return ApiResponse.success_only(SuccessStatus.OK);
    }

    @GetMapping("/requested")
    public ResponseEntity<ApiResponse<Page<OrderResDto>>> getRequestedOrders(@RequestParam String from,
                                                                             Pageable pageable) {
        return ApiResponse.success(SuccessStatus.OK, orderService.getOrders(from, pageable));
    }
}
