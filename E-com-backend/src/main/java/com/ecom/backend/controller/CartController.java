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
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private CheckoutService checkoutService;

    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    // TODO: refactor with session object
    @GetMapping
    public ResponseEntity<?> getCart(@RequestParam(required = false) String sessionId,
                                     Principal principal){
        String userId = principal.getName();
        Cart cart = cartService.getOrCreateCart(userId, sessionId);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addItemToCart(@RequestBody Cart.CartItem cartItem, Principal principal) {
        String userId = principal.getName();
        cartService.addItemToCart(userId, cartItem);
        return ResponseEntity.ok("Item added to cart");
    }

    @PostMapping("/remove")
    public ResponseEntity<String> removeItemFromCart(@RequestParam(required = true) String productId, Principal principal) {
        String userId = principal.getName();
        cartService.removeItemFromCart(userId, productId);
        return ResponseEntity.ok("Item removed from cart");
    }

    @DeleteMapping
    public ResponseEntity<String> clearCart(Principal principal) {
        String userId = principal.getName();
        cartService.clearCart(userId);
        return ResponseEntity.ok("Cart cleared");
    }

    // transactional ?
    @PostMapping("/checkout")
    public ResponseEntity<Void> convertCartToOrder(@RequestBody CheckoutRequest request,
                                                    UriComponentsBuilder ucb, Principal principal) {
        Order order = checkoutService.checkoutCart(request);
        URI uri = ucb.path("/orders/{id}").buildAndExpand(order.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }
}

/* TODO: when item is available when cart is created, but now item is sold out
    add more items on same type and remove them
    what to do with price changes
 */