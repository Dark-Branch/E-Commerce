package com.ecom.backend.service;

import com.ecom.backend.exception.NotFoundException;
import com.ecom.backend.model.Cart;
import com.ecom.backend.repository.CartRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final ProductService productService;

    public CartService(ProductService productService, CartRepository cartRepository) {
        this.productService = productService;
        this.cartRepository = cartRepository;
    }

    // test
    public Cart getCartById(String id){
        return cartRepository.findById(id).orElseThrow(() -> new NotFoundException("Cart not found"));
    }

    public Cart createCart(Cart cart){
        return cartRepository.save(cart);
    }

    public Cart createCart() {
        Date date = new Date();
        Cart newCart = Cart.builder()
                .active(true)
                .items(new ArrayList<>())
                .createdAt(date)
                .updatedAt(date)
                .build();
        return cartRepository.save(newCart);
    }

    public void deleteCart(Cart cart){
        cartRepository.delete(cart);
    }

    public void clearCart(String cartId) {
        Cart cart = getCartById(cartId);
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    public void addItemToCart(String cartId, Cart.CartItem cartItem) {
        Cart cart = getCartById(cartId);

        List<Cart.CartItem> items = cart.getItems();
        boolean itemUpdated = false;

        for (Cart.CartItem item : items) {
            if (item.getProductId().equals(cartItem.getProductId())) {
                item.setQuantity(item.getQuantity() + cartItem.getQuantity());
                itemUpdated = true;
                break;
            }
        }

        if (!itemUpdated) {
            items.add(new Cart.CartItem(cartItem.getProductId(), cartItem.getQuantity(), cartItem.getVersion(), productService.getPriceByProductId(cartItem.getProductId())));
        }

        cart.setItems(items);
        cartRepository.save(cart);
    }

    public void removeItemFromCart(String cartId, String productId) {
        Cart cart = getCartById(cartId);

        boolean itemExists = cart.getItems().stream()
                .anyMatch(item -> item.getProductId().equals(productId));

        if (!itemExists) {
            throw new NotFoundException("Item not found in cart");
        }

        List<Cart.CartItem> updatedItems = cart.getItems().stream()
                .filter(item -> !item.getProductId().equals(productId))
                .toList();

        cart.setItems(updatedItems);
        cartRepository.save(cart);
    }

    public Cart getOrCreateCart(String userId, String sessionId) {
        Cart cart;
        if (userId != null) {
            // registered users -> find cart by userId
            cart = cartRepository.findByUserId(userId)
                    .orElseGet(() -> {
                        return createCartWithUserId(userId);
                    });
        } else if (sessionId != null) {
            // guest -> find cart by sessionId
            cart = cartRepository.findBySessionId(sessionId)
                    .orElseGet(() -> {
                        return createCartWithSessionId(sessionId);
                    });
        } else {
            throw new RuntimeException("Either userId or sessionId must be provided");
        }
        return cart;
    }

    private Cart createCartWithSessionId(String sessionId) {
        Date date = new Date();
        Cart newCart = Cart.builder()
                .sessionId(sessionId)
                .active(true)
                .items(new ArrayList<>())
                .createdAt(date)
                .updatedAt(date)
                .build();
        return cartRepository.save(newCart);
    }

    private Cart createCartWithUserId(String userId) {
        Date date = new Date();
        Cart newCart = Cart.builder()
                .userId(userId)
                .active(true)
                .items(new ArrayList<>())
                .createdAt(date)
                .updatedAt(date)
                .build();
        return cartRepository.save(newCart);
    }

    public Cart updateCart(Cart cart) {

        if (!cartRepository.existsById(cart.getId())) {
            throw new NotFoundException("Cart not found with ID: " + cart.getId());
        }

        cart.setUpdatedAt(new Date());

        return cartRepository.save(cart);
    }
}
// TODO: handle cartBuilder, using createdAt and UpdatedAt