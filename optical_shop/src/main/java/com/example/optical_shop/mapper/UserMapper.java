package com.example.optical_shop.mapper;

import com.example.optical_shop.dto.UserDto;
import com.example.optical_shop.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface UserMapper {
    UserDto userToUserDto(User user);
}
