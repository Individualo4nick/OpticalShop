package com.example.optical_shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="shopping_cart")
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    private int count = 1;
    public ShoppingCart(User user, Product product) {
        this.user = user;
        this.product = product;
    }
    public ShoppingCart() {

    }
    public void incrementCount(){
        this.count++;
    }
}

