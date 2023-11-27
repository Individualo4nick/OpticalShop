package com.example.optical_shop.repository;

import com.example.optical_shop.entity.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    Optional<ShoppingCart> findShoppingCartByProductId(Long productId);
    List<ShoppingCart> findAllByUserId(Long userid);
    ShoppingCart findShoppingCartByUserIdAndId(Long userId, Long id);
    @Transactional
    void deleteByProductId(Long productId); //This method works before the method for deleting a product
}
