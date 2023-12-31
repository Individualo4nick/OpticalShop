package com.example.optical_shop.controller;

import com.example.optical_shop.dto.*;
import com.example.optical_shop.entity.Product;
import com.example.optical_shop.mapper.CommentMapper;
import com.example.optical_shop.mapper.ProductMapper;
import com.example.optical_shop.service.ProductService;
import com.example.optical_shop.service.ShoppingCartService;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;
    private final ShoppingCartService shoppingCartService;
    private final ProductMapper productMapper = Mappers.getMapper(ProductMapper.class);
    private final CommentMapper commentMapper = Mappers.getMapper(CommentMapper.class);

    public ProductController(ProductService productService, ShoppingCartService shoppingCartService) {
        this.productService = productService;
        this.shoppingCartService = shoppingCartService;
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        return ResponseEntity.ok(productMapper.listProductToListProductDto(productService.getAllProductWithCountLargerZero()));
    }
    @PostMapping
    public ResponseEntity<PageResponse> getFilterProducts(@RequestBody FilterRequest filterRequest){
        Page<Product> page;
        if (filterRequest.page != null && filterRequest.size != null)
            page = productService.getFilterProduct(PageRequest.of(filterRequest.page, filterRequest.size), filterRequest.category);
        else if (filterRequest.page != null)
            page = productService.getFilterProduct(PageRequest.of(filterRequest.page, 20), filterRequest.category);
        else if (filterRequest.size != null)
            page = productService.getFilterProduct(PageRequest.of(0, filterRequest.size), filterRequest.category);
        else
            page = productService.getFilterProduct(PageRequest.of(0, 20), filterRequest.category);
        return ResponseEntity.ok(new PageResponse().setContent(productMapper.listProductToListProductDto(page.getContent())).setPage(page.getNumber()).setSize(page.getSize()));
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        Optional<Product> product = productService.getProductById(id);
        if(product.isEmpty())
            return ResponseEntity.status(400).body(Responser.getResponse("Product with id " + id + " not found"));
        return ResponseEntity.ok(productMapper.productToProductDto(product.get()));
    }

    @PostMapping("/add_to_cart/{id}")
    public ResponseEntity<ResponseDto> addToShoppingCart(@PathVariable Long id, @RequestBody LoginDto loginDto) {
        boolean success = shoppingCartService.addProduct(loginDto.login, id);
        if (success)
            return ResponseEntity.ok(Responser.getResponse("Added"));
        return ResponseEntity.status(400).body(Responser.getResponse("Error add. This item may be out of stock"));
    }

    @PostMapping("/comment/{product_id}")
    public ResponseEntity<ResponseDto> sendComment(@PathVariable Long product_id, @RequestBody AddCommentDto addCommentDto) {
        boolean success = productService.addComment(product_id, addCommentDto.text, addCommentDto.login);
        if (success)
            return ResponseEntity.ok(Responser.getResponse("Sent"));
        return ResponseEntity.status(400).body(Responser.getResponse("Error send"));
    }

    @GetMapping("/comment/{product_id}")
    public ResponseEntity<List<CommentDto>> getComments(@PathVariable Long product_id) {
        return ResponseEntity.ok(commentMapper.listCommentToListCommentDto(productService.getComments(product_id)));
    }
}
