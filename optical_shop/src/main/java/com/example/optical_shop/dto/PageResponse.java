package com.example.optical_shop.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class PageResponse {
    public List<ProductDto> content;
    public int page;
    public int size;
    public long totalPages;
}
