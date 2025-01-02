package com.ecom.backend.service;

import com.ecom.backend.model.Cart;
import com.ecom.backend.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        return cartRepository.findById(id).orElseThrow(() -> new RuntimeException("No cart"));
    }

    public Cart createCart(Cart cart){
        return cartRepository.save(cart);
    }

    public Cart getCartByUserId(String userId) {
        return cartRepository.findByUserId(userId);
    }

    public Cart addToCart(String userId, Cart.CartItem cartItem) {
        Cart cart = cartRepository.findByUserId(userId);
        if (cart == null) {
            cart = new Cart();
            cart.setUserId(userId);
        }
        cart.getItems().add(cartItem);
        return cartRepository.save(cart);
    }

    public void clearCart(String userId) {
        Cart cart = cartRepository.findByUserId(userId);
        if (cart != null) {
            cart.getItems().clear();
            cartRepository.save(cart);
        }
    }

    public void addItemToCart(String userId, String productId, int quantity) {
        // Check product stock
        if (!productService.isProductInStock(productId, quantity)) {
            throw new RuntimeException("Product is out of stock or requested quantity exceeds available stock");
        }

        // Find the cart for the user
        Cart cart = cartRepository.findByUserId(userId);
        if (cart == null)
            cart = new Cart(null, userId, new ArrayList<>());

        // Update or add the item in the cart
        List<Cart.CartItem> items = cart.getItems();
        boolean itemUpdated = false;

        for (Cart.CartItem item : items) {
            if (item.getProductId().equals(productId)) {
                item.setQuantity(item.getQuantity() + quantity);
                itemUpdated = true;
                break;
            }
        }

        if (!itemUpdated) {
            items.add(new Cart.CartItem(productId, quantity, null));
        }

        cart.setItems(items);
        cartRepository.save(cart);
    }

    public void removeItemFromCart(String userId, String productId) {
        // Find the cart for the user
        Cart cart = cartRepository.findByUserId(userId);
        if (cart == null)
            throw new RuntimeException("Cart not found");

        // Remove the item
        cart.setItems(cart.getItems().stream()
                .filter(item -> !item.getProductId().equals(productId))
                .toList());

        cartRepository.save(cart);
    }
}
