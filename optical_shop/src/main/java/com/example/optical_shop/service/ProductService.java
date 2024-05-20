package com.example.optical_shop.service;

import com.example.optical_shop.entity.Comment;
import com.example.optical_shop.entity.Product;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> getAllProductWithCountLargerZero();
    Optional<Product> getProductById(Long id);
    Comment addComment(Long product_id, String text, String login);
    List<Comment> getComments(Long product_id);
    Product saveProduct(Product product);
    void deleteComment(Long id);
    void deleteProduct(Long id);
    void updateProduct(Product product);
    Page<Product> getFilterProduct(Pageable pageable, String category);

    void uploadProductImage(Long id, MultipartFile multipartFile) throws IOException;

    Resource downloadProductImage(Long id) throws IOException;

    void deleteProductImage(Long id);

    Page<Product> getFavoriteProducts(Pageable pageable, String category, List<Long> ids);
}
