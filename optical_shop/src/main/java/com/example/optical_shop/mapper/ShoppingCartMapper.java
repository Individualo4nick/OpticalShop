package com.example.optical_shop.mapper;

import com.example.optical_shop.dto.ProductDto;
import com.example.optical_shop.dto.ShoppingCartDto;
import com.example.optical_shop.entity.Product;
import com.example.optical_shop.entity.ShoppingCart;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {
    ProductMapper productMapper = Mappers.getMapper(ProductMapper.class);
    @Mapping(target = "productDto", source = "product")
    ShoppingCartDto cartToCartDto(ShoppingCart shoppingCart);
    default ProductDto map(Product product) {
        return productMapper.productToProductDto(product);
    }
    @IterableMapping(elementTargetType = ShoppingCartDto.class)
    List<ShoppingCartDto> listCartsToListCartsDto(Iterable<ShoppingCart> carts);
}
