package com.example.gateway.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeUserInfoDto {
    public String email;
    public String name;
    public String surname;
    public String address;
}
