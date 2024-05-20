package com.example.optical_shop.controller;

import com.example.optical_shop.dto.CreateOrderDto;
import com.example.optical_shop.dto.LoginDto;
import com.example.optical_shop.dto.OrderDto;
import com.example.optical_shop.dto.ResponseDto;
import com.example.optical_shop.entity.Order;
import com.example.optical_shop.mapper.OrderMapper;
import com.example.optical_shop.mapper.ProductMapper;
import com.example.optical_shop.service.OrderService;
import org.mapstruct.factory.Mappers;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;
    private final OrderMapper orderMapper = Mappers.getMapper(OrderMapper.class);

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createOrder(@RequestBody CreateOrderDto createOrderDto) {
        orderService.createOrder(createOrderDto.cartIds, createOrderDto.login, createOrderDto.price);
        return ResponseEntity.ok(Responser.getResponse("Created"));
    }

    @GetMapping
    public ResponseEntity<List<OrderDto>> getOrders() {
        var orders = orderMapper.listOrderToListOrderDto(orderService.getAllOrders());
        orders.sort(Comparator.comparing(o -> o.id));
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{login}")
    public ResponseEntity<List<OrderDto>> getOrdersByLogin(@PathVariable String login) {
        return ResponseEntity.ok(orderMapper.listOrderToListOrderDto(orderService.getAllOrdersByLogin(login)));
    }
}
