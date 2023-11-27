package com.example.optical_shop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShoppingCartDto {
    public Long id;
    public ProductDto productDto;
    public int count;
}
