package com.example.optical_shop.controller;

import com.example.optical_shop.dto.*;
import com.example.optical_shop.entity.Product;
import com.example.optical_shop.mapper.CommentMapper;
import com.example.optical_shop.mapper.ProductMapper;
import com.example.optical_shop.service.ProductService;
import com.example.optical_shop.service.ShoppingCartService;
import org.mapstruct.factory.Mappers;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
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

    @GetMapping("/all")
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<ProductDto> productDtoList = productMapper.listProductToListProductDto(productService.getAllProductWithCountLargerZero());
        productDtoList.sort(Comparator.comparing(o -> o.title));
        return ResponseEntity.ok(productDtoList);
    }

    @PostMapping("/favorites")
    public ResponseEntity<PageResponse> getProductsByIds(@RequestBody ListProductIdsDto listProductIdsDto, @RequestParam String category, @RequestParam int page, @RequestParam int size){
        Page<Product> productPage = productService.getFavoriteProducts(PageRequest.of(page == 0 ? 0 : page - 1, size), category, listProductIdsDto.ids.stream().map(Long::parseLong).toList());
        List<ProductDto> productDtoList = productMapper.listProductToListProductDto(productPage.getContent());
        productDtoList.sort(Comparator.comparing(o -> o.title));
        return ResponseEntity.ok(new PageResponse().setContent(productDtoList).setPage(productPage.getNumber()).setSize(productPage.getSize()).setTotalPages(productPage.getTotalPages()));
    }

    @GetMapping
    public ResponseEntity<PageResponse> getFilterProducts(@RequestParam String category, @RequestParam int page, @RequestParam int size){
        Page<Product> productPage = productService.getFilterProduct(PageRequest.of(page == 0 ? 0 : page - 1, size), category);
        List<ProductDto> productDtoList = productMapper.listProductToListProductDto(productPage.getContent());
        productDtoList.sort(Comparator.comparing(o -> o.title));
        return ResponseEntity.ok(new PageResponse().setContent(productDtoList).setPage(productPage.getNumber()).setSize(productPage.getSize()).setTotalPages(productPage.getTotalPages()));
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        Optional<Product> product = productService.getProductById(id);
        if(product.isEmpty())
            return ResponseEntity.status(400).body(Responser.getResponse("Product with id " + id + " not found"));
        return ResponseEntity.ok(productMapper.productToProductDto(product.get()));
    }

    @PostMapping("/add/{id}")
    public ResponseEntity<ResponseDto> addToShoppingCart(@PathVariable Long id, @RequestBody LoginDto loginDto) {
        boolean success = shoppingCartService.addProduct(loginDto.login, id);
        if (success)
            return ResponseEntity.ok(Responser.getResponse("Added"));
        return ResponseEntity.status(400).body(Responser.getResponse("Error add. This item may be out of stock"));
    }

    @GetMapping("/image/download/{id}")
    public ResponseEntity<Resource> downloadProductImage(@PathVariable Long id) throws IOException {
        Resource resource = productService.downloadProductImage(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PostMapping("/comment/{product_id}")
    public ResponseEntity<?> sendComment(@PathVariable Long product_id, @RequestBody AddCommentDto addCommentDto) {
        var comment = productService.addComment(product_id, addCommentDto.text, addCommentDto.login);
        if (comment != null)
            return ResponseEntity.ok(commentMapper.commentToCommentDto(comment));
        return ResponseEntity.status(400).body(Responser.getResponse("Error send"));
    }

    @GetMapping("/comment/{product_id}")
    public ResponseEntity<List<CommentDto>> getComments(@PathVariable Long product_id) {
        return ResponseEntity.ok(commentMapper.listCommentToListCommentDto(productService.getComments(product_id)));
    }
    @ExceptionHandler({IOException.class})
    public ResponseEntity<String> handleUserRegistrationExceptionForData(Exception ex) {
        return ResponseEntity
                .badRequest()
                .body("Error");
    }
}
