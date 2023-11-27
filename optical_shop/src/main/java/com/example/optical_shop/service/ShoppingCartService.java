package com.example.optical_shop.service;

import com.example.optical_shop.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {
    boolean addProduct(String login, Long product_id);
    List<ShoppingCart> getUserCart(String login);
    void deleteProductFromCart(String login, Long id);
}
