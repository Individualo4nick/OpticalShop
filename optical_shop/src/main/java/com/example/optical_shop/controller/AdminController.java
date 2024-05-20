package com.example.optical_shop.controller;

import com.example.optical_shop.dto.CreateOrderDto;
import com.example.optical_shop.dto.ProductDto;
import com.example.optical_shop.dto.ResponseDto;
import com.example.optical_shop.dto.UpdateOrderDto;
import com.example.optical_shop.entity.Order;
import com.example.optical_shop.mapper.ProductMapper;
import com.example.optical_shop.service.OrderService;
import com.example.optical_shop.service.ProductService;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final ProductService productService;
    private final OrderService orderService;
    private final ProductMapper productMapper = Mappers.getMapper(ProductMapper.class);

    public AdminController(ProductService productService, OrderService orderService) {
        this.productService = productService;
        this.orderService = orderService;
    }

    @PostMapping("/product")
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(productMapper.productToProductDto(productService.saveProduct(productMapper.productDtoToProduct(productDto))));
    }
    @PostMapping("/product/image/upload/{id}")
    public ResponseEntity<ResponseDto> uploadProductImage(@PathVariable Long id, @RequestBody MultipartFile multipartFile) throws IOException {
        productService.uploadProductImage(id, multipartFile);
        return ResponseEntity.ok(Responser.getResponse("Uploaded"));
    }
    @DeleteMapping("/product/image/delete/{id}")
    public ResponseEntity<ResponseDto> deleteProductImage(@PathVariable Long id) throws IOException {
        productService.deleteProductImage(id);
        return ResponseEntity.ok(Responser.getResponse("Deleted"));
    }
    @DeleteMapping("/comment/{comment_id}")
    public ResponseEntity<ResponseDto> deleteComment(@PathVariable Long comment_id) {
        productService.deleteComment(comment_id);
        return ResponseEntity.ok(Responser.getResponse("Deleted comment"));
    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<ResponseDto> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        productService.deleteProductImage(id);
        return ResponseEntity.ok(Responser.getResponse("Deleted product"));
    }

    @PutMapping("/product")
    public ResponseEntity<ResponseDto> updateProduct(@RequestBody ProductDto productDto) {
        productService.updateProduct(productMapper.productDtoToProduct(productDto));
        return ResponseEntity.ok(Responser.getResponse("Successful updated"));
    }

    @PutMapping("/order")
    public ResponseEntity<ResponseDto> updateOrder(@RequestBody UpdateOrderDto updateOrderDto) {
        orderService.updateOrder(updateOrderDto.orderId, Order.OrderStateEnum.valueOf(updateOrderDto.orderState));
        return ResponseEntity.ok(Responser.getResponse("Updated"));
    }
}
