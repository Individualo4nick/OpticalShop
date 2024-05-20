package com.example.optical_shop.service.impl;

import com.example.optical_shop.entity.Order;
import com.example.optical_shop.entity.User;
import com.example.optical_shop.repository.OrderRepository;
import com.example.optical_shop.repository.ShoppingCartRepository;
import com.example.optical_shop.repository.UserRepository;
import com.example.optical_shop.service.OrderService;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Component
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final UserRepository userRepository;

    public OrderServiceImpl(OrderRepository orderRepository, ShoppingCartRepository shoppingCartRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void createOrder(List<Long> cartIds, String login, int price) {
        var carts = shoppingCartRepository.findAllByIdIn(cartIds);
        var order = new Order();
        order.setUser(userRepository.findUserByLogin(login).get());
        order.setPrice(price);
        order.setOrderDate(Instant.now());
        order.setOrderState(Order.OrderStateEnum.NEW);
        orderRepository.save(order);
        for (var cart : carts) {
            if (cart.getOrder() == null) {
                cart.setOrder(order);
                shoppingCartRepository.save(cart);
            }
        }
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public List<Order> getAllOrdersByLogin(String login) {
        Optional<User> user = userRepository.findUserByLogin(login);
        return orderRepository.findAllByUserId(user.get().getId());
    }

    @Override
    public void updateOrder(Long orderId, Order.OrderStateEnum orderState) {
        Order order = orderRepository.findById(orderId).get();
        order.setOrderState(orderState);
        orderRepository.save(order);
    }
}
