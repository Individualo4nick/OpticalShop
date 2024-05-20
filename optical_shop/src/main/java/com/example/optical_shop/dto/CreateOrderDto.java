package com.example.optical_shop.dto;

import java.util.List;

public class CreateOrderDto {
    public List<Long> cartIds;
    public String login;
    public int price;
}
