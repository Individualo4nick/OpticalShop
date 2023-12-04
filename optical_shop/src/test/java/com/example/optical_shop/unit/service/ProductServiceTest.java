package com.example.optical_shop.unit.service;

import com.example.optical_shop.dto.ProductDto;
import com.example.optical_shop.entity.Comment;
import com.example.optical_shop.entity.Product;
import com.example.optical_shop.entity.User;
import com.example.optical_shop.repository.CommentRepository;
import com.example.optical_shop.repository.ProductRepository;
import com.example.optical_shop.repository.ShoppingCartRepository;
import com.example.optical_shop.repository.UserRepository;
import com.example.optical_shop.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @InjectMocks
    private ProductServiceImpl productService;


    @Test
    void getAllProductWithCountLargerZero_ExistingProducts_ReturnsListOfProducts() {
        List<Product> expectedProducts = new ArrayList<>();
        Product product1 = new Product();
        product1.setCount(1);
        expectedProducts.add(product1);

        when(productRepository.findAllWithCountLargerZero()).thenReturn(expectedProducts);

        List<Product> result = productService.getAllProductWithCountLargerZero();

        assertEquals(expectedProducts, result);
        verify(productRepository).findAllWithCountLargerZero();  // Проверяем, что вызвался метод findAllWithCountLargerZero в productRepository
    }

    @Test
    void getProductById_ExistingProduct_ReturnsProduct() {
        Long productId = 1L;
        Product expectedProduct = new Product();
        expectedProduct.setId(productId);

        when(productRepository.findById(productId)).thenReturn(Optional.of(expectedProduct));

        Optional<Product> result = productService.getProductById(productId);

        assertTrue(result.isPresent());
        assertEquals(expectedProduct, result.get());
        verify(productRepository).findById(productId);  // Проверяем, что вызвался метод findById в productRepository
    }

    @Test
    void getProductById_NonExistingProduct_ReturnsEmptyOptional() {
        Long productId = 1L;

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        Optional<Product> result = productService.getProductById(productId);

        assertTrue(result.isEmpty());
        verify(productRepository).findById(productId);  // Проверяем, что вызвался метод findById в productRepository
    }

    @Test
    void addComment_ValidInputs_CommentIsAdded() {
        Long productId = 1L;
        String text = "Test comment";
        String login = "test_user";

        User user = new User();
        user.setLogin(login);

        Product product = new Product();
        product.setId(productId);

        when(userRepository.findUserByLogin(login)).thenReturn(Optional.of(user));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        boolean result = productService.addComment(productId, text, login);

        assertTrue(result);
        verify(userRepository).findUserByLogin(login);  // Проверяем, что вызвался метод findUserByLogin в userRepository
        verify(productRepository).findById(productId);  // Проверяем, что вызвался метод findById в productRepository
        verify(commentRepository).save(any(Comment.class));  // Проверяем, что вызвался метод save в commentRepository с аргументом типа Comment
    }

    @Test
    void addComment_InvalidUser_ReturnsFalse() {
        Long productId = 1L;
        String text = "Test comment";
        String login = "non_existing_user";

        when(userRepository.findUserByLogin(login)).thenReturn(Optional.empty());

        boolean result = productService.addComment(productId, text, login);

        assertFalse(result);
        verify(userRepository).findUserByLogin(login);  // Проверяем, что вызвался метод findUserByLogin в userRepository
        verify(commentRepository, never()).save(any(Comment.class));  // Проверяем, что метод save в commentRepository не вызывался
    }

    @Test
    void addComment_InvalidProduct_ReturnsFalse() {
        Long productId = 1L;
        String text = "Test comment";
        String login = "test_user";

        User user = new User();
        user.setLogin(login);

        when(userRepository.findUserByLogin(login)).thenReturn(Optional.of(user));
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        boolean result = productService.addComment(productId, text, login);

        assertFalse(result);
        verify(userRepository).findUserByLogin(login);  // Проверяем, что вызвался метод findUserByLogin в userRepository
        verify(productRepository).findById(productId);  // Проверяем, что вызвался метод findById в productRepository
        verify(commentRepository, never()).save(any(Comment.class));  // Проверяем, что метод save в commentRepository не вызывался
    }

    @Test
    void getComments_ExistingProductId_ReturnsListOfComments() {
        Long productId = 1L;
        List<Comment> expectedComments = new ArrayList<>();
        expectedComments.add(new Comment());
        expectedComments.add(new Comment());

        when(commentRepository.findAllByProductId(productId)).thenReturn(expectedComments);

        List<Comment> result = productService.getComments(productId);

        assertEquals(expectedComments, result);
        verify(commentRepository).findAllByProductId(productId);  // Проверяем, что вызвался метод findAllByProductId в commentRepository
    }

    @Test
    void saveProduct_ValidProduct_ProductIsSaved() {
        Product product = new Product();

        productService.saveProduct(product);

        verify(productRepository).save(product);  // Проверяем, что вызвался метод save в productRepository с аргументом типа Product
    }

    @Test
    void deleteComment_ExistingCommentId_CommentIsDeleted() {
        Long commentId = 1L;

        productService.deleteComment(commentId);

        verify(commentRepository).deleteById(commentId);  // Проверяем, что вызвался метод deleteById в commentRepository с аргументом типа Long
    }

    @Test
    void updateProduct_ValidProductDto_ProductInfoIsUpdated() {
        Product product = new Product();

        productService.updateProduct(product);

        verify(productRepository).changeProductInfo(product);  // Проверяем, что вызвался метод changeProductInfo в productRepository с аргументом типа ProductDto
    }
}
