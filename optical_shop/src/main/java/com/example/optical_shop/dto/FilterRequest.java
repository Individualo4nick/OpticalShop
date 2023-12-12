package com.example.optical_shop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class FilterRequest {
    public String category;
    public Integer page;
    public Integer size;
}