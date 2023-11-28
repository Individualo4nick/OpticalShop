package com.example.optical_shop.controller;

import com.example.optical_shop.dto.ProductDto;
import com.example.optical_shop.dto.ResponseDto;
import com.example.optical_shop.mapper.ProductMapper;
import com.example.optical_shop.service.ProductService;
import org.mapstruct.factory.Mappers;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final ProductService productService;
    private final ProductMapper productMapper = Mappers.getMapper(ProductMapper.class);

    public AdminController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/product")
    public ResponseEntity<ResponseDto> createProduct(@RequestBody ProductDto productDto){
        productService.saveProduct(productMapper.productDtoToProduct(productDto));
        return ResponseEntity.ok(Responser.getResponse("Saved"));
    }
    @DeleteMapping("/comment/{comment_id}")
    public ResponseEntity<ResponseDto> deleteComment(@PathVariable Long comment_id) {
        productService.deleteComment(comment_id);
        return ResponseEntity.ok(Responser.getResponse("Deleted comment"));
    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<ResponseDto> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(Responser.getResponse("Deleted product"));
    }

    @PutMapping("/product")
    public ResponseEntity<ResponseDto> updateProduct(@RequestBody ProductDto productDto) {
        productService.updateProduct(productDto);
        return ResponseEntity.ok(Responser.getResponse("Successful updated"));
    }
}
