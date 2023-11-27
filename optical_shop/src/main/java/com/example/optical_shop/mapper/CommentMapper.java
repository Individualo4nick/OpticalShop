package com.example.optical_shop.mapper;

import com.example.optical_shop.dto.CommentDto;
import com.example.optical_shop.dto.ProductDto;
import com.example.optical_shop.entity.Comment;
import com.example.optical_shop.entity.Product;
import com.example.optical_shop.entity.User;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface CommentMapper {
    @Mapping(target = "fio", source = "user")
    CommentDto commentToCommentDto(Comment comment);
    default String map(User field) {
        return field.getName() + ' ' + field.getSurname();
    }
    @IterableMapping(elementTargetType = CommentDto.class)
    List<CommentDto> listCommentToListCommentDto(Iterable<Comment> comments);
}
