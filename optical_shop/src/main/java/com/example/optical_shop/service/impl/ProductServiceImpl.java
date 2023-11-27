package com.example.optical_shop.service.impl;

import com.example.optical_shop.entity.Comment;
import com.example.optical_shop.entity.Product;
import com.example.optical_shop.entity.User;
import com.example.optical_shop.repository.CommentRepository;
import com.example.optical_shop.repository.ProductRepository;
import com.example.optical_shop.repository.UserRepository;
import com.example.optical_shop.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CommentRepository commentRepository;

    public ProductServiceImpl(UserRepository userRepository, ProductRepository productRepository, CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public List<Product> getAllProductWithCountLargerZero() {
        return productRepository.findAllWithCountLargerZero();
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public boolean addComment(Long product_id, String text, String login) {
        Optional<User> user = userRepository.findUserByLogin(login);
        Optional<Product> product = productRepository.findById(product_id);
        if (user.isPresent() && product.isPresent()) {
            Comment comment = new Comment(user.get(), product_id, text);
            commentRepository.save(comment);
            return true;
        }
        return false;
    }

    @Override
    public List<Comment> getComments(Long product_id) {
        return commentRepository.findAllByProductId(product_id);
    }

    @Override
    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    @Override
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
