package com.example.optical_shop.dto;

import com.example.optical_shop.entity.User;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDto {
    public Long id;
    public String login;
    public String text;
}
