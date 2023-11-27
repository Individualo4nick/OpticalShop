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

//    @PostMapping("/increment_in_shopping_cart")
//    public String incrementInShoppingCart(@RequestParam Long id) {
//        shoppingCartService.incrementInShoppingCart(id);
//        return "redirect:/component/shopping_cart";
//    }
//
//    @PostMapping("/decrement_in_shopping_cart")
//    public String decrementInShoppingCart(@RequestParam Long id) {
//        shoppingCartService.decrementInShoppingCart(id);
//        return "redirect:/component/shopping_cart";
//    }
}
