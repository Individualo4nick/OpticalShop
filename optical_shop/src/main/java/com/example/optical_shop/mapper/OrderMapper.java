package com.example.optical_shop.mapper;

import com.example.optical_shop.dto.CommentDto;
import com.example.optical_shop.dto.OrderDto;
import com.example.optical_shop.dto.ShoppingCartDto;
import com.example.optical_shop.entity.Comment;
import com.example.optical_shop.entity.Order;
import com.example.optical_shop.entity.ShoppingCart;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface OrderMapper {
    ShoppingCartMapper shoppingCartMapper = Mappers.getMapper(ShoppingCartMapper.class);
    @Mapping(target = "cartsDto", source = "shoppingCarts")
    OrderDto orderToOrderDto(Order order);
    default List<ShoppingCartDto> map(List<ShoppingCart> field) {
        if (field == null) {
            return null;
        } else {
            List<ShoppingCartDto> shoppingCartDtos = new ArrayList<>();
            for (ShoppingCart shoppingCart : field) {
                shoppingCartDtos.add(shoppingCartMapper.cartToCartDto(shoppingCart));
            }
            return shoppingCartDtos;
        }
    }
    @IterableMapping(elementTargetType = OrderDto.class)
    List<OrderDto> listOrderToListOrderDto(Iterable<Order> orders);
}

