package com.example.optical_shop.repository;

import com.example.optical_shop.entity.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    Optional<ShoppingCart> findShoppingCartByProductIdAndUserIdAndOrderIsNull(Long productId, Long userId);
    Optional<ShoppingCart> findShoppingCartByProductIdAndUserIdAndOrderIsNotNull(Long productId, Long userId);
    List<ShoppingCart> findAllByUserIdAndOrderIsNull(Long userid);
    ShoppingCart findShoppingCartByUserIdAndId(Long userId, Long id);
    @Transactional
    void deleteByProductId(Long productId); //This method works before the method for deleting a product
    @Transactional
    void deleteByUserLogin(String userLogin);
    List<ShoppingCart> findAllByIdIn(List<Long> ids);
}
