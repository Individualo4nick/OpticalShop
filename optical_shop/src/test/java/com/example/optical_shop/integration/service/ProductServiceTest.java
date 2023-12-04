package com.example.optical_shop.integration.service;

import com.example.optical_shop.entity.Comment;
import com.example.optical_shop.entity.Product;
import static org.assertj.core.api.Assertions.assertThat;
import com.example.optical_shop.integration.IntegrationTestBase;
import com.example.optical_shop.repository.CommentRepository;
import com.example.optical_shop.repository.ProductRepository;
import com.example.optical_shop.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

public class ProductServiceTest extends IntegrationTestBase {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Test
    void getAllProductWithCountLargerZero_ExistingProducts_ReturnsListOfProducts() {
        List<Product> result = productService.getAllProductWithCountLargerZero();
        assertEquals(4, result.size());
        List<Product> expectedResult = productRepository.findAllWithCountLargerZero();
        assertThat(result.get(0)).usingRecursiveComparison().isEqualTo(expectedResult.get(0));
        assertThat(result.get(1)).usingRecursiveComparison().isEqualTo(expectedResult.get(1));
        assertThat(result.get(2)).usingRecursiveComparison().isEqualTo(expectedResult.get(2));
        assertThat(result.get(3)).usingRecursiveComparison().isEqualTo(expectedResult.get(3));
    }

    @Test
    void getProductById_ExistingProduct_ReturnsProduct() {
        Optional<Product> result = productService.getProductById(1L);
        assertTrue(result.isPresent());
        Optional<Product> expectedResult = productRepository.findById(1L);
        assertThat(result).usingRecursiveComparison().isEqualTo(expectedResult);
    }

    @Test
    void getProductById_NonExistingProduct_ReturnsEmptyOptional() {
        Optional<Product> result = productService.getProductById(6L);
        assertTrue(result.isEmpty());
        Optional<Product> expectedResult = productRepository.findById(6L);
        assertThat(result).usingRecursiveComparison().isEqualTo(expectedResult);
    }

    @Test
    void addComment_ValidInputs_CommentIsAdded() {
        Long productId = 1L;
        String text = "Test comment";
        String login = "user1";

        boolean result = productService.addComment(productId, text, login);
        assertTrue(result);
        List<Comment> comments = commentRepository.findAll();
        Comment lastComment = comments.get(comments.size() - 1);
        assertEquals(productId, lastComment.getProductId());
        assertEquals(text, lastComment.getText());
        assertEquals(login, lastComment.getUser().getLogin());
    }

    @Test
    void addComment_InvalidUser_ReturnsFalse() {
        Long productId = 1L;
        String text = "Test comment";
        String login = "non_existing_user";

        boolean result = productService.addComment(productId, text, login);
        assertFalse(result);
    }

    @Test
    void addComment_InvalidProduct_ReturnsFalse() {
        Long productId = 6L;
        String text = "Test comment";
        String login = "user1";

        boolean result = productService.addComment(productId, text, login);
        assertFalse(result);
    }

    @Test
    void getComments_ExistingProductId_ReturnsListOfComments() {
        Long productId = 1L;
        List<Comment> result = productService.getComments(productId);
        assertEquals(1, result.size());
        List<Comment> expectedResult = commentRepository.findAllByProductId(productId);
        assertThat(result.get(0)).usingRecursiveComparison().isEqualTo(expectedResult.get(0));
    }

    @Test
    void saveProduct_ValidProduct_ProductIsSaved() {
        Product product = new Product();
        product.setTitle("new_title");
        product.setDescription("new_description");
        product.setCategory("new_category");
        product.setPrice(100);
        productService.saveProduct(product);
        List<Product> products = productRepository.findAll();
        Product lastProduct = products.get(products.size() - 1);
        assertThat(product).usingRecursiveComparison().isEqualTo(lastProduct);
    }

    @Test
    void deleteComment_ExistingCommentId_CommentIsDeleted() {
        Long commentId = 1L;
        productService.deleteComment(commentId);
        assertTrue(commentRepository.findById(commentId).isEmpty());
    }

    @Test
    void updateProduct_ValidProductDto_ProductInfoIsUpdated() {
        Product product = new Product();
        product.setId(1L);
        product.setTitle("new_title");
        product.setDescription("new_description");
        product.setCategory("new_category");
        product.setPrice(100);
        productService.updateProduct(product);
        Product expectedProduct = productRepository.findById(product.getId()).get();
        assertThat(product).usingRecursiveComparison().isEqualTo(expectedProduct);
    }
}
