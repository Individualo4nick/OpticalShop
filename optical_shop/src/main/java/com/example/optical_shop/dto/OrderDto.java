package com.example.optical_shop.dto;

import com.example.optical_shop.entity.Order;

import java.time.Instant;
import java.util.List;

public class OrderDto {
    public Long id;
    public int price;
    public Instant orderDate;
    public Order.OrderStateEnum orderState;
    public List<ShoppingCartDto> cartsDto;
}
