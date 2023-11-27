package com.example.optical_shop.service;

import com.example.optical_shop.entity.Comment;
import com.example.optical_shop.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> getAllProductWithCountLargerZero();
    Optional<Product> getProductById(Long id);
    boolean addComment(Long product_id, String text, String login);
    List<Comment> getComments(Long product_id);
    void saveProduct(Product product);
    void deleteComment(Long id);
    void deleteProduct(Long id);
}
