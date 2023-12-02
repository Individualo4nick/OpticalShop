package com.example.optical_shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long productId;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String text;

    public Comment(User user, Long productId, String text) {
        this.productId = productId;
        this.user = user;
        this.text = text;
    }

    public Comment() {

    }
}
