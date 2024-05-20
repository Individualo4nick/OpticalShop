package com.example.optical_shop.service;

import com.example.optical_shop.entity.Order;

import java.util.List;

public interface OrderService {
    void createOrder(List<Long> cartIds, String login, int price);
    List<Order> getAllOrders();
    List<Order> getAllOrdersByLogin(String login);

    void updateOrder(Long orderId, Order.OrderStateEnum orderState);
}
