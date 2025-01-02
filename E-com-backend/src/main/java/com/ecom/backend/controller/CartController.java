package com.ecom.backend.controller;

import com.ecom.backend.model.Cart;
import com.ecom.backend.model.Product;
import com.ecom.backend.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @GetMapping("/{id}")
    public ResponseEntity<Cart> getCartById(@PathVariable String id){
        Cart cart = cartService.getCartById(id);
        if (cart != null) {
            return ResponseEntity.ok(cart);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
//  this is the real one
//    @GetMapping("/{userId}")
//    public Cart getCartByUserId(@PathVariable String userId) {
//        return cartService.getCartByUserId(userId);
//    }

    @PostMapping
    public ResponseEntity<Void> createCart(@RequestBody Cart cart, UriComponentsBuilder ucb){
        Cart newCart =  cartService.createCart(cart);
        URI uri = ucb.path("/cart/{id}").buildAndExpand(newCart.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PostMapping("/{userId}")
    public Cart addToCart(@PathVariable String userId, @RequestBody Cart.CartItem cartItem) {
        return cartService.addToCart(userId, cartItem);
    }

    @DeleteMapping("/{userId}")
    public void clearCart(@PathVariable String userId) {
        cartService.clearCart(userId);
    }
}
