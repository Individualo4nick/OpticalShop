package com.example.optical_shop.integration.service;

import com.example.optical_shop.dto.ProductDto;
import com.example.optical_shop.entity.Comment;
import com.example.optical_shop.entity.Product;
import com.example.optical_shop.entity.User;
import com.example.optical_shop.integration.IntegrationTestBase;
import com.example.optical_shop.integration.annotation.IntegrationTest;
import com.example.optical_shop.repository.CommentRepository;
import com.example.optical_shop.repository.ProductRepository;
import com.example.optical_shop.repository.ShoppingCartRepository;
import com.example.optical_shop.repository.UserRepository;
import com.example.optical_shop.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@RequiredArgsConstructor
public class ProductServiceTest extends IntegrationTestBase {

    @Autowired
    private ProductService productService;

    @Test
    void test(){
        System.out.println(productService.getProductById(1l).get().getTitle());
    }

}
