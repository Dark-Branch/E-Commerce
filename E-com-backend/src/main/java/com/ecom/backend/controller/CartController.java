package com.ecom.backend.controller;

import com.ecom.backend.DTO.CheckoutRequest;
import com.ecom.backend.model.Cart;
import com.ecom.backend.model.Order;
import com.ecom.backend.service.CartService;
import com.ecom.backend.service.CheckoutService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.security.Principal;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private CheckoutService checkoutService;

    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    // TODO: refactor with session object
    @GetMapping
    public ResponseEntity<?> getCart(
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String sessionId){
        Cart cart = cartService.getOrCreateCart(userId, sessionId);
        return ResponseEntity.ok(cart);
    }

    // TODO: remove if no use
    @PostMapping
    public ResponseEntity<Void> createCart(@RequestBody Cart cart, UriComponentsBuilder ucb){
        Cart newCart =  cartService.createCart(cart);
        URI uri = ucb.path("/cart/{id}").buildAndExpand(newCart.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    // transactional ?
    @PostMapping("/checkout")
    public ResponseEntity<Order> convertCartToOrder(@RequestBody CheckoutRequest request,
                                                    UriComponentsBuilder ucb, Principal principal){
        Order order = checkoutService.checkoutCart(request);
        URI uri = ucb.path("/orders/{id}").buildAndExpand(order.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    // FIXME: for now i get userId from client -> change when implementing security
    @PostMapping("/add")
    public ResponseEntity<String> addItemToCart(@RequestBody Cart.CartItem cartItem,
                                                @RequestParam(required = false) String userId,
                                                @RequestParam(required = false) String sessionId){

        cartService.addItemToCart(userId, sessionId, cartItem);
        return ResponseEntity.ok("Item added to cart");
    }

    @PostMapping("/remove")
    public ResponseEntity<String> removeItemFromCart(@RequestParam String productId,
                                                     @RequestParam(required = false) String userId,
                                                     @RequestParam(required = false) String sessionId){
        cartService.removeItemFromCart(userId, sessionId, productId);
        return ResponseEntity.ok("Item removed from cart");
    }

    @DeleteMapping("/{userId}")
    public void clearCart(@PathVariable String userId) {
        cartService.clearCart(userId);
    }
}

/* TODO: when item is available when cart is created, but now item is sold out
    add more items on same type and remove them
    what to do with price changes
 */