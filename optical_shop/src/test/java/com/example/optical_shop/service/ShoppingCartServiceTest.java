package com.example.optical_shop.service;

import com.example.optical_shop.entity.Product;
import com.example.optical_shop.entity.ShoppingCart;
import com.example.optical_shop.entity.User;
import com.example.optical_shop.repository.ProductRepository;
import com.example.optical_shop.repository.ShoppingCartRepository;
import com.example.optical_shop.repository.UserRepository;
import com.example.optical_shop.service.impl.ShoppingCartServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ShoppingCartServiceTest {

    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        shoppingCartService = new ShoppingCartServiceImpl(shoppingCartRepository, userRepository, productRepository);
    }

    @Test
    void addProduct_ValidInputsAndAvailableProduct_ReturnsTrueAndShoppingCartIsUpdated() {
        String login = "test_user";
        Long productId = 1L;

        User user = new User();
        user.setLogin(login);

        Product product = new Product();
        product.setId(productId);
        product.setCount(1);

        ShoppingCart shoppingCart = new ShoppingCart(user, product);
        shoppingCart.setCount(1);

        when(userRepository.findUserByLogin(login)).thenReturn(Optional.of(user));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(shoppingCartRepository.findShoppingCartByProductId(productId)).thenReturn(Optional.of(shoppingCart));

        boolean result = shoppingCartService.addProduct(login, productId);

        assertTrue(result);
        assertEquals(0, product.getCount());
        assertEquals(2, shoppingCart.getCount());
        verify(userRepository).findUserByLogin(login);
        verify(productRepository).findById(productId);
        verify(productRepository).save(product);
        verify(shoppingCartRepository).save(shoppingCart);
    }

    @Test
    void addProduct_ValidInputsAndUnavailableProduct_ReturnsFalseAndShoppingCartIsNotUpdated() {
        String login = "test_user";
        Long productId = 1L;

        User user = new User();
        user.setLogin(login);

        Product product = new Product();
        product.setId(productId);
        product.setCount(0);

        ShoppingCart shoppingCart = new ShoppingCart(user, product);
        shoppingCart.setCount(1);

        when(userRepository.findUserByLogin(login)).thenReturn(Optional.of(user));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(shoppingCartRepository.findShoppingCartByProductId(productId)).thenReturn(Optional.of(shoppingCart));

        boolean result = shoppingCartService.addProduct(login, productId);

        assertFalse(result);
        assertEquals(0, product.getCount());
        assertEquals(1, shoppingCart.getCount());
        verify(userRepository).findUserByLogin(login);
        verify(productRepository).findById(productId);
        verify(productRepository, never()).save(product);
        verify(shoppingCartRepository, never()).save(shoppingCart);
    }

    @Test
    void addProduct_InvalidUser_ReturnsFalseAndShoppingCartIsNotUpdated() {
        String login = "non_existing_user";
        Long productId = 1L;

        when(userRepository.findUserByLogin(login)).thenReturn(Optional.empty());

        boolean result = shoppingCartService.addProduct(login, productId);

        assertFalse(result);
        verify(userRepository).findUserByLogin(login);
        verify(shoppingCartRepository, never()).findShoppingCartByProductId(anyLong());
        verify(productRepository, never()).save(any(Product.class));
        verify(shoppingCartRepository, never()).save(any(ShoppingCart.class));
    }

    @Test
    void addProduct_InvalidProduct_ReturnsFalseAndShoppingCartIsNotUpdated() {
        String login = "test_user";
        Long productId = 1L;

        User user = new User();
        user.setLogin(login);

        when(userRepository.findUserByLogin(login)).thenReturn(Optional.of(user));
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        boolean result = shoppingCartService.addProduct(login, productId);

        assertFalse(result);
        verify(userRepository).findUserByLogin(login);
        verify(productRepository).findById(productId);
        verify(shoppingCartRepository, never()).findShoppingCartByProductId(anyLong());
        verify(productRepository, never()).save(any(Product.class));
        verify(shoppingCartRepository, never()).save(any(ShoppingCart.class));
    }

    @Test
    void getUserCart_ExistingUser_ReturnsListOfShoppingCartItems() {
        String login = "test_user";
        User user = new User();
        user.setLogin(login);

        List<ShoppingCart> expectedShoppingCart = new ArrayList<>();
        expectedShoppingCart.add(new ShoppingCart());
        expectedShoppingCart.add(new ShoppingCart());

        when(userRepository.findUserByLogin(login)).thenReturn(Optional.of(user));
        when(shoppingCartRepository.findAllByUserId(user.getId())).thenReturn(expectedShoppingCart);

        List<ShoppingCart> result = shoppingCartService.getUserCart(login);

        assertEquals(expectedShoppingCart, result);
        verify(userRepository).findUserByLogin(login);
        verify(shoppingCartRepository).findAllByUserId(user.getId());
    }

    @Test
    void deleteProductFromCart_ExistingUserAndShoppingCart_ShoppingCartAndProductAreUpdated() {
        String login = "test_user";
        Long shoppingCartId = 1L;

        User user = new User();
        user.setLogin(login);

        Product product = new Product();
        product.setCount(1);

        ShoppingCart shoppingCart = new ShoppingCart(user, product);
        shoppingCart.setId(shoppingCartId);
        shoppingCart.setCount(1);

        when(userRepository.findUserByLogin(login)).thenReturn(Optional.of(user));
        when(shoppingCartRepository.findShoppingCartByUserIdAndId(user.getId(), shoppingCartId)).thenReturn(shoppingCart);

        shoppingCartService.deleteProductFromCart(login, shoppingCartId);

        assertEquals(2, product.getCount());
        verify(userRepository).findUserByLogin(login);
        verify(shoppingCartRepository).findShoppingCartByUserIdAndId(user.getId(), shoppingCartId);
        verify(productRepository).save(product);
        verify(shoppingCartRepository).delete(shoppingCart);
    }

    @Test
    void pay_ExistingUser_ShoppingCartIsDeleted() {
        String login = "test_user";
        User user = new User();
        user.setLogin(login);

        shoppingCartService.pay(login);

        verify(shoppingCartRepository).deleteByUserLogin(login);
    }
}
