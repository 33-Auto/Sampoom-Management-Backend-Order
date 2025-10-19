package com.sampoom.backend.api.order.service;

import com.sampoom.backend.api.order.dto.ItemDto;
import com.sampoom.backend.api.order.dto.OrderReqDto;
import com.sampoom.backend.api.order.dto.OrderResDto;
import com.sampoom.backend.api.order.dto.ToWarehouseDto;
import com.sampoom.backend.api.order.entity.Order;
import com.sampoom.backend.api.order.entity.OrderStatus;
import com.sampoom.backend.api.order.entity.OrderType;
import com.sampoom.backend.api.order.entity.Requester;
import com.sampoom.backend.api.order.repository.OrderRepository;
import com.sampoom.backend.api.order.sender.WarehouseClient;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
public class OrderServiceTest {
    @Autowired
    private OrderService orderService;
    @MockBean
    private WarehouseClient warehouseClient;
    @Autowired
    private OrderRepository orderRepository;

    @Test
    void should_create_order() {
        Requester requester = Requester.AGENCY;
        String branch = "branch";
        OrderType type = OrderType.PART;
        List<ItemDto> items = List.of(
                ItemDto.builder()
                        .code("ENG-01-001")
                        .quantity(2)
                        .build(),
                ItemDto.builder()
                        .code("ENG-01-002")
                        .quantity(3)
                        .build()
        );;
        OrderReqDto req = OrderReqDto.builder()
                .requester(requester)
                .branch(branch)
                .type(type)
                .items(items)
                .build();

        Order savedOrder = Order.builder()
                .branch(branch)
                .requester(requester)
                .type(type)
                .status(OrderStatus.PENDING)
                .build();

//        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);
        orderRepository.save(savedOrder);

        // when
        OrderResDto orderResDto = orderService.createOrder(req);

        // then
        assertThat(orderResDto).isNotNull();
        assertThat(orderResDto.getStatus()).isEqualTo(OrderStatus.PENDING);
        assertThat(orderRepository.findById(orderResDto.getId())).isPresent();

        // warehouseClient 호출 검증
        verify(warehouseClient).sendOrderToWarehouse(any(ToWarehouseDto.class));
    }
}
