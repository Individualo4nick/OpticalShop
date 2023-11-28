package com.example.optical_shop.controller;

import com.example.optical_shop.dto.ResponseDto;
import com.example.optical_shop.dto.ShoppingCartDto;
import com.example.optical_shop.mapper.ShoppingCartMapper;
import com.example.optical_shop.service.ShoppingCartService;
import org.mapstruct.factory.Mappers;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;
    private final ShoppingCartMapper shoppingCartMapper = Mappers.getMapper(ShoppingCartMapper.class);

    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @GetMapping("/{login}")
    public ResponseEntity<List<ShoppingCartDto>> getShoppingCart(@PathVariable String login) {
        return ResponseEntity.ok(shoppingCartMapper.listCartsToListCartsDto(shoppingCartService.getUserCart(login)));
    }

    @DeleteMapping("/{login}/{id}")
    public ResponseEntity<ResponseDto> getShoppingCart(@PathVariable String login, @PathVariable Long id) {
        shoppingCartService.deleteProductFromCart(login, id);
        return ResponseEntity.ok(Responser.getResponse("Successful deleted"));
    }

    @PostMapping("/pay/{login}")
    public ResponseEntity<ResponseDto> pay(@PathVariable String login) {
        shoppingCartService.pay(login);
        return ResponseEntity.ok(Responser.getResponse("Successful paid"));
    }
}
