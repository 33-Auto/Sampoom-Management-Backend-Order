package com.sampoom.backend.api.order.service;

import com.sampoom.backend.api.order.dto.ItemDto;
import com.sampoom.backend.api.order.dto.OrderReqDto;
import com.sampoom.backend.api.order.dto.OrderResDto;
import com.sampoom.backend.api.order.entity.OrderStatus;
import com.sampoom.backend.api.order.entity.OrderType;
import com.sampoom.backend.api.order.entity.Requester;
import com.sampoom.backend.api.order.repository.OrderRepository;
import com.sampoom.backend.api.warehouse.WarehouseClient;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.awaitility.Awaitility.given;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class OrderServiceTest {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;

    @Test
    void should_create_order_when_inventory_is_sufficient() {
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

        // when
        OrderResDto orderResDto = orderService.createOrder(req);

        // then
        assertThat(orderResDto).isNotNull();
        assertThat(orderResDto.getStatus()).isEqualTo(OrderStatus.PENDING);
        assertThat(orderRepository.findById(orderResDto.getId())).isPresent();
    }
}
