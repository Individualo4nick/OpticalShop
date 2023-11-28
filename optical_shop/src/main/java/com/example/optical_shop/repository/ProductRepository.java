package com.example.optical_shop.repository;

import com.example.optical_shop.dto.ProductDto;
import com.example.optical_shop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query(value = "select * from products where count > 0", nativeQuery = true)
    List<Product> findAllWithCountLargerZero();

    @Transactional
    @Modifying
    @Query("update Product p set p.title = :#{#dto.title}, p.description = :#{#dto.description}, p.category = :#{#dto.category}, p.price = :#{#dto.price}, p.count = :#{#dto.count} where p.id = :#{#dto.id}")
    void changeProductInfo(@Param("dto") ProductDto productDto);
}
