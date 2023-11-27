package com.example.optical_shop.repository;

import com.example.optical_shop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query(value = "select * from products where count > 0", nativeQuery = true)
    List<Product> findAllWithCountLargerZero();
}
