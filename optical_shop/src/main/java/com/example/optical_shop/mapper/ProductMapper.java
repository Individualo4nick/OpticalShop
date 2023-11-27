package com.example.optical_shop.mapper;

import com.example.optical_shop.dto.ProductDto;
import com.example.optical_shop.entity.Product;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface ProductMapper {
    @Mapping(target = "title", source = "title")
    ProductDto productToProductDto(Product product);
    Product productDtoToProduct(ProductDto productDto);
    @IterableMapping(elementTargetType = ProductDto.class)
    List<ProductDto> listProductToListProductDto(Iterable<Product> products);
}
