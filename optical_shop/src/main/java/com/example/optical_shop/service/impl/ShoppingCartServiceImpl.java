package com.example.optical_shop.service.impl;

import com.example.optical_shop.entity.Product;
import com.example.optical_shop.entity.ShoppingCart;
import com.example.optical_shop.entity.User;
import com.example.optical_shop.repository.ProductRepository;
import com.example.optical_shop.repository.ShoppingCartRepository;
import com.example.optical_shop.repository.UserRepository;
import com.example.optical_shop.service.ShoppingCartService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public ShoppingCartServiceImpl(ShoppingCartRepository shoppingCartRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Override
    public boolean addProduct(String login, Long product_id) {
        Optional<User> user = userRepository.findUserByLogin(login);
        Optional<Product> product = productRepository.findById(product_id);
        if (user.isPresent() && product.isPresent() && product.get().getCount() > 0) {
            Optional<ShoppingCart> shoppingCart = shoppingCartRepository.findShoppingCartByProductId(product_id);
            ShoppingCart shoppingCart1;
            Product product1 = product.get();
            product1.decrement_count();
            productRepository.save(product1);
            if(shoppingCart.isPresent()){
                shoppingCart1 = shoppingCart.get();
                shoppingCart1.incrementCount();
            }
            else {
                shoppingCart1 = new ShoppingCart(user.get(), product.get());
            }
            shoppingCartRepository.save(shoppingCart1);
            return true;
        }
        return false;
    }
}
