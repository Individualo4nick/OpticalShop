package com.example.optical_shop.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FilterRequest {
    public String category;
    public Integer page;
    public Integer size;
}