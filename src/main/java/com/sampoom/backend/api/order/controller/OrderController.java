package com.sampoom.backend.api.order.controller;

import com.sampoom.backend.api.order.dto.ItemDto;
import com.sampoom.backend.api.order.dto.OrderReqDto;
import com.sampoom.backend.api.order.dto.OrderResDto;
import com.sampoom.backend.api.order.entity.Requester;
import com.sampoom.backend.api.order.service.OrderService;
import com.sampoom.backend.common.response.ApiResponse;
import com.sampoom.backend.common.response.SuccessStatus;
import lombok.RequiredArgsConstructor;
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
        orderService.sendOrderToDownstream(order);

        return ApiResponse.success(SuccessStatus.OK, orderResDto);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderResDto>> getOrder(@PathVariable Long orderId) {
        List<ItemDto> items = List.of(
                ItemDto.builder()
                        .code("ENG-01-001")
                        .quantity(2)
                        .build(),
                ItemDto.builder()
                        .code("ENG-01-002")
                        .quantity(3)
                        .build()
        );

        OrderResDto orderResDto = OrderResDto.builder()
                .id(orderId)
                .items(items)
                .requester(Requester.WAREHOUSE)
                .branch("강원도")
                .build();

        return ApiResponse.success(SuccessStatus.OK, orderResDto);
    }

    @GetMapping("/requested")
    public ResponseEntity<ApiResponse<List<OrderResDto>>> getRequestedOrders(@RequestParam String from) {
        return ApiResponse.success(SuccessStatus.OK, orderService.getOrders(from));
    }
}
