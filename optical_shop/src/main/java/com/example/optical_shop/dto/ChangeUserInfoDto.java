package com.example.optical_shop.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChangeUserInfoDto {
    public String email;
    public String name;
    public String surname;
    public String address;

    public ChangeUserInfoDto(String email) {
        this.email = email;
    }
}
