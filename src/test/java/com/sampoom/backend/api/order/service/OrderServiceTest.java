package com.sampoom.backend.api.order.service;

import com.sampoom.backend.api.order.dto.ItemPartDto;
import com.sampoom.backend.api.order.dto.OrderReqDto;
import com.sampoom.backend.api.order.dto.OrderResDto;
import com.sampoom.backend.api.order.dto.ToWarehouseDto;
import com.sampoom.backend.api.order.entity.OrderStatus;
import com.sampoom.backend.api.order.entity.Requester;
import com.sampoom.backend.api.order.repository.OrderRepository;
import com.sampoom.backend.api.order.sender.WarehouseClient;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

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
        List<ItemPartDto> items = List.of(
                ItemPartDto.builder()
                        .code("ENG-01-001")
                        .quantity(2)
                        .build(),
                ItemPartDto.builder()
                        .code("ENG-01-002")
                        .quantity(3)
                        .build()
        );;
        OrderReqDto req = OrderReqDto.builder()
                .requester(requester)
                .branch(branch)
                .items(items)
                .build();

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
