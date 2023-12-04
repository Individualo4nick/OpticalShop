package com.example.optical_shop.integration.service;

import com.example.optical_shop.entity.ShoppingCart;
import com.example.optical_shop.integration.IntegrationTestBase;
import com.example.optical_shop.repository.ShoppingCartRepository;
import com.example.optical_shop.service.ShoppingCartService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ShoppingCartServiceTest extends IntegrationTestBase {

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Test
    void addProduct_ValidInputsAndAvailableProduct_ReturnsTrueAndShoppingCartIsUpdated() {
        String login = "user1";
        Long productId = 1L;

        boolean result = shoppingCartService.addProduct(login, productId);

        assertTrue(result);
    }

    @Test
    void addProduct_ValidInputsAndUnavailableProduct_ReturnsFalseAndShoppingCartIsNotUpdated() {
        String login = "user1";
        Long productId = 5L;

        boolean result = shoppingCartService.addProduct(login, productId);

        assertFalse(result);
    }

    @Test
    void addProduct_InvalidUser_ReturnsFalseAndShoppingCartIsNotUpdated() {
        String login = "non_existing_user";
        Long productId = 1L;

        boolean result = shoppingCartService.addProduct(login, productId);

        assertFalse(result);
    }

    @Test
    void addProduct_InvalidProduct_ReturnsFalseAndShoppingCartIsNotUpdated() {
        String login = "user1";
        Long productId = 6L;

        boolean result = shoppingCartService.addProduct(login, productId);

        assertFalse(result);
    }

    @Test
    void getUserCart_ExistingUser_ReturnsListOfShoppingCartItems() {
        String login = "user1";
        List<ShoppingCart> result = shoppingCartService.getUserCart(login);

        assertEquals(login, result.get(0).getUser().getLogin());
        assertEquals(1L, result.get(0).getUser().getId());
        assertEquals(1L, result.get(0).getProduct().getId());
        assertEquals(1, result.get(0).getCount());

        assertEquals(login, result.get(1).getUser().getLogin());
        assertEquals(1L, result.get(1).getUser().getId());
        assertEquals(3L, result.get(1).getProduct().getId());
        assertEquals(1, result.get(1).getCount());
    }

    @Test
    void deleteProductFromCart_ExistingUserAndShoppingCart_ShoppingCartAndProductAreUpdated() {
        String login = "user1";
        Long shoppingCartId = 1L;

        shoppingCartService.deleteProductFromCart(login, shoppingCartId);

        assertTrue(shoppingCartRepository.findById(shoppingCartId).isEmpty());
    }

    @Test
    void pay_ExistingUser_ShoppingCartIsDeleted() {
        String login = "user1";
        Long id = 1L;

        shoppingCartService.pay(login);

        assertTrue(shoppingCartRepository.findAllByUserId(id).isEmpty());
    }
}
