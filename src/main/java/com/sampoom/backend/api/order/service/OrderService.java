package com.sampoom.backend.api.order.service;

import com.sampoom.backend.api.order.dto.*;
import com.sampoom.backend.api.order.entity.Order;
import com.sampoom.backend.api.order.entity.OrderPart;
import com.sampoom.backend.api.order.entity.OrderStatus;
import com.sampoom.backend.api.order.entity.EventOutbox;
import com.sampoom.backend.api.order.event.OrderCancelEvent;
import com.sampoom.backend.api.order.event.ToWarehouseEvent;
import com.sampoom.backend.api.order.repository.EventOutboxRepository;
import com.sampoom.backend.api.order.repository.OrderRepository;
import com.sampoom.backend.common.exception.BadRequestException;
import com.sampoom.backend.common.exception.NotFoundException;
import com.sampoom.backend.common.response.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderPartService orderPartService;
    private final EventOutboxRepository eventOutboxRepository;

    @Transactional
    public OrderResDto createOrder(OrderReqDto orderReqDto) {
        Order newOrder = Order.builder()
                .orderNumber(this.makeOrderName())
                .branch(orderReqDto.getAgencyName())
                .status(OrderStatus.PENDING)
                .build();
        orderRepository.saveAndFlush(newOrder);
        orderPartService.saveAllParts(newOrder.getId(), orderReqDto.getItems());

        List<ItemBriefDto> briefDtos = orderReqDto.getItems().stream()
                .flatMap(c -> c.getGroups().stream())
                .flatMap(g -> g.getParts().stream())
                .map(p -> ItemBriefDto.builder()
                        .code(p.getCode())
                        .quantity(p.getQuantity())
                        .build())
                .toList();

        EventOutbox newEventOutbox = EventOutbox.builder()
                .topic("sales-events")
                .payload(ToWarehouseEvent.builder()
                        .orderId(newOrder.getId())
                        .branch(orderReqDto.getAgencyName())
                        .items(briefDtos)
                        .version(newOrder.getVersion())
                        .sourceUpdatedAt(newOrder.getCreatedAt().atOffset(ZoneOffset.ofHours(9)))
                        .build())
                .build();
        eventOutboxRepository.saveAndFlush(newEventOutbox);

        return OrderResDto.builder()
                .orderId(newOrder.getId())
                .orderNumber(newOrder.getOrderNumber())
                .agencyName(orderReqDto.getAgencyName())
                .status(OrderStatus.PENDING)
                .items(orderReqDto.getItems())
                .createdAt(newOrder.getCreatedAt().toString())
                .build();
    }

    @Transactional(readOnly = true)
    public Page<OrderResDto> getOrders(String from, int page, int size) {
        final String normalizedBranch = StringUtils.hasText(from) ? from.trim() : null;
        Pageable pageable = PageRequest.of(page, size);
        Page<Order> orders = orderRepository.findWithItemsByBranch(normalizedBranch, pageable);

        return orders.map(order -> OrderResDto.builder()
                .orderId(order.getId())
                .orderNumber(order.getOrderNumber())
                .agencyName(order.getBranch())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt().toString())
                .items(this.convertOrderItems(order.getOrderParts()))
                .build());
    }

    private String makeOrderName() {
        String uuidPart = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String today = LocalDate.now(ZoneOffset.ofHours(9)).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return "ORD-" + today + "-" + uuidPart;
    }

    @Transactional(readOnly = true)
    public OrderResDto getOrder(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new NotFoundException(ErrorStatus.ORDER_NOT_FOUND.getMessage())
        );

        return OrderResDto.builder()
                .orderId(order.getId())
                .orderNumber(order.getOrderNumber())
                .agencyName(order.getBranch())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt().toString())
                .items(this.convertOrderItems(order.getOrderParts()))
                .build();
    }

    private List<ItemCategoryDto> convertOrderItems(List<OrderPart> orderParts) {
        Map<Long, ItemCategoryDto> categoryMap = new LinkedHashMap<>();

        for (OrderPart part : orderParts) {
            // 카테고리 처리
            ItemCategoryDto category = categoryMap.computeIfAbsent(
                    part.getCategoryId(),
                    cid -> new ItemCategoryDto(
                            cid,
                            part.getCategoryName(),
                            new ArrayList<>()
                    )
            );

            // 그룹 처리
            ItemGroupDto group = category.getGroups().stream()
                    .filter(g -> g.getGroupId().equals(part.getGroupId()))
                    .findFirst()
                    .orElseGet(() -> {
                        ItemGroupDto newGroup = new ItemGroupDto(
                                part.getGroupId(),
                                part.getGroupName(),
                                new ArrayList<>()
                        );
                        category.getGroups().add(newGroup);
                        return newGroup;
                    });

            // 파트 추가
            group.getParts().add(
                    new ItemPartDto(
                            part.getPartId(),
                            part.getName(),
                            part.getCode(),
                            part.getQuantity()
                    )
            );
        }

        return new ArrayList<>(categoryMap.values());
    }

    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new NotFoundException(ErrorStatus.ORDER_NOT_FOUND.getMessage())
        );

        if (order.getStatus() == OrderStatus.SHIPPING)
            throw new BadRequestException(ErrorStatus.SHIPPING_CANT_CANCEL.getMessage());
        else if (order.getStatus() == OrderStatus.CANCELED)
            throw new BadRequestException(ErrorStatus.ALREADY_CANCELED.getMessage());
        else if (order.getStatus() == OrderStatus.COMPLETED)
            throw new BadRequestException(ErrorStatus.ALREADY_COMPLETED_ORDER.getMessage());

        order.setStatus(OrderStatus.CANCELED);
        orderRepository.save(order);

        EventOutbox eventOutbox = EventOutbox.builder()
                .topic("order-cancel-events")
                .payload(OrderCancelEvent.builder()
                        .orderId(order.getId())
                        .warehouseId(order.getWarehouseId())
                        .parts(order.getOrderParts().stream()
                                .map(op -> ItemBriefDto.builder()
                                        .code(op.getCode())
                                        .quantity(op.getQuantity())
                                        .build())
                                .toList())
                        .build())
                .build();
        eventOutboxRepository.save(eventOutbox);
    }

    @Transactional
    public void updateOrderStatus(OrderStatusEvent orderStatusEvent) {
        Order order = orderRepository.findById(orderStatusEvent.getOrderId()).orElseThrow(
                () -> new NotFoundException(ErrorStatus.ORDER_NOT_FOUND.getMessage())
        );

        order.setStatus(orderStatusEvent.getOrderStatus());
        orderRepository.save(order);
    }

    @Transactional
    public void allocateWarehouse(OrderWarehouseEvent orderWarehouseEvent) {
        Order order = orderRepository.findById(orderWarehouseEvent.getOrderId()).orElseThrow(
                () -> new NotFoundException(ErrorStatus.ORDER_NOT_FOUND.getMessage())
        );

        order.setWarehouseId(orderWarehouseEvent.getWarehouseId());
        order.setWarehouseName(orderWarehouseEvent.getWarehouseName());
        orderRepository.save(order);
    }

}
