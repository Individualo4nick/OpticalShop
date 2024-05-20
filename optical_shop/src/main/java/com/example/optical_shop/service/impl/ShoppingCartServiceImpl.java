package com.example.optical_shop.service.impl;

import com.example.optical_shop.entity.Product;
import com.example.optical_shop.entity.ShoppingCart;
import com.example.optical_shop.entity.User;
import com.example.optical_shop.repository.ProductRepository;
import com.example.optical_shop.repository.ShoppingCartRepository;
import com.example.optical_shop.repository.UserRepository;
import com.example.optical_shop.service.ShoppingCartService;
import org.springframework.stereotype.Service;

import java.util.List;
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
    public boolean addProduct(String login, Long productId) {
        Optional<User> user = userRepository.findUserByLogin(login);
        Optional<Product> product = productRepository.findById(productId);
        if (user.isPresent() && product.isPresent() && product.get().getCount() > 0) {
            Optional<ShoppingCart> shoppingCart = shoppingCartRepository.findShoppingCartByProductIdAndUserIdAndOrderIsNull(productId, user.get().getId());
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

    @Override
    public List<ShoppingCart> getUserCart(String login) {
        User user = userRepository.findUserByLogin(login).get();
        return shoppingCartRepository.findAllByUserIdAndOrderIsNull(user.getId());
    }

    @Override
    public void deleteProductFromCart(String login, Long id) {
        User user = userRepository.findUserByLogin(login).get();
        ShoppingCart shoppingCart = shoppingCartRepository.findShoppingCartByUserIdAndId(user.getId(), id);
        Product product = shoppingCart.getProduct();
        int count = shoppingCart.getCount();
        product.setCount(product.getCount()+count);
        productRepository.save(product);
        shoppingCartRepository.delete(shoppingCart);
    }

    @Override
    public void pay(String login) {
        shoppingCartRepository.deleteByUserLogin(login);
    }

    @Override
    public void increaseInCart(String login, Long id) {
        User user = userRepository.findUserByLogin(login).get();
        ShoppingCart shoppingCart = shoppingCartRepository.findShoppingCartByUserIdAndId(user.getId(), id);
        shoppingCart.setCount(shoppingCart.getCount() + 1);
        shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public void decreaseInCart(String login, Long id) {
        User user = userRepository.findUserByLogin(login).get();
        ShoppingCart shoppingCart = shoppingCartRepository.findShoppingCartByUserIdAndId(user.getId(), id);
        if (shoppingCart.getCount() - 1 == 0){
            shoppingCartRepository.deleteById(shoppingCart.getId());
        }
        else {
            shoppingCart.setCount(shoppingCart.getCount() - 1);
            shoppingCartRepository.save(shoppingCart);
        }
    }
}
