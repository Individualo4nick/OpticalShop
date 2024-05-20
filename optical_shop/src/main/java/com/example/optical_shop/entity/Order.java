package com.example.optical_shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @OneToMany(mappedBy = "order")
    private List<ShoppingCart> shoppingCarts;
    private int price;
    private Instant orderDate;
    @Enumerated(value = EnumType.STRING)
    private OrderStateEnum orderState;
    public enum OrderStateEnum{
        NEW,
        DELIVERED,
        COMPLETED,
        CANCELED
    }
}
