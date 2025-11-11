package com.sampoom.backend.api.order.controller;

import com.sampoom.backend.api.order.dto.OrderReqDto;
import com.sampoom.backend.api.order.dto.OrderResDto;
import com.sampoom.backend.api.order.dto.OrderWithStockDto;
import com.sampoom.backend.api.order.dto.OutboundFilterDto;
import com.sampoom.backend.api.order.entity.OrderStatus;
import com.sampoom.backend.api.order.service.OrderService;
import com.sampoom.backend.common.response.ApiResponse;
import com.sampoom.backend.common.response.SuccessStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResDto>> createOrder(@Valid @RequestBody OrderReqDto order) {
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

    @PatchMapping("/complete/{orderId}")
    public ResponseEntity<ApiResponse<Void>> completeOrder(@PathVariable Long orderId) {
        orderService.completeOrder(orderId);
        return ApiResponse.success_only(SuccessStatus.OK);
    }

    @GetMapping("/requested")
    public ResponseEntity<ApiResponse<Page<OrderResDto>>> getRequestedOrders(@RequestParam String from,
                                                                             @RequestParam(defaultValue = "0") int page,
                                                                             @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.success(SuccessStatus.OK, orderService.getOrders(from, page, size));
    }

    @GetMapping("/warehouse/{warehouseId}")
    public ResponseEntity<ApiResponse<Page<OrderResDto>>> getWarehouseOrders(@PathVariable Long warehouseId,
                                                                             @RequestParam(required = false) String from,
                                                                             @RequestParam(required = false) OrderStatus status,
                                                                             @RequestParam(defaultValue = "0") int page,
                                                                             @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.success(SuccessStatus.OK, orderService.getOrdersForWarehouse(warehouseId, from, status, page, size));
    }

    @GetMapping("/outbound")
    public ResponseEntity<ApiResponse<Page<OrderWithStockDto>>> getOutboundList(@RequestParam Long warehouseId,
                                                                                @RequestParam(required = false) Long categoryId,
                                                                                @RequestParam(required = false) Long groupId,
                                                                                @RequestParam(required = false) String keyword,
                                                                                @RequestParam(required = false) OrderStatus status,
                                                                                @RequestParam(defaultValue = "0") int page,
                                                                                @RequestParam(defaultValue = "20") int size,
                                                                                @CookieValue("ACCESS_TOKEN") String token) {
        OutboundFilterDto outboundFilterDto = OutboundFilterDto.builder()
                .warehouseId(warehouseId)
                .categoryId(categoryId)
                .groupId(groupId)
                .keyword(keyword)
                .orderStatus(status)
                .build();
        Pageable pageable = PageRequest.of(page, size);

        return ApiResponse.success(SuccessStatus.OK, orderService.getOrdersForOutbound(outboundFilterDto, pageable, token));
    }
}
