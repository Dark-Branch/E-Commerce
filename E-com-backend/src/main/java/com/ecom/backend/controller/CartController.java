package com.ecom.backend.controller;

import com.ecom.backend.model.Cart;
import com.ecom.backend.service.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    @GetMapping("/{id}")
    public ResponseEntity<Cart> getCartById(@PathVariable String id){
        Cart cart;
        try {
            cart = cartService.getCartById(id);
            return ResponseEntity.ok(cart);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }catch (Exception e) {
            logger.error("Internal server error: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
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

    @PatchMapping("/{id}/addItem")
    public ResponseEntity<Cart> addToCartByCartId(@PathVariable String id, @RequestBody Cart.CartItem cartItem) {

        cartService.addItemToCart(id, cartItem);
        Cart updatedCart = cartService.getCartById(id);

        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping("/{userId}")
    public void clearCart(@PathVariable String userId) {
        cartService.clearCart(userId);
    }
}

/* TODO: when item is available when cart is created, but now item is sold out
    add more items on same type and remove them
 */