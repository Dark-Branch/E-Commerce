package com.ecom.backend.service;

import com.ecom.backend.exception.NotFoundException;
import com.ecom.backend.model.Cart;
import com.ecom.backend.repository.CartRepository;
import org.springframework.stereotype.Service;

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

    public void deleteCart(Cart cart){
        cartRepository.delete(cart);
    }

    public Cart getCartByUserName(String userName) {
        return cartRepository.findByUserName(userName);
    }

    public Cart addToCart(String userName, Cart.CartItem cartItem) {
        Cart cart = cartRepository.findByUserName(userName);
        if (cart == null) {
            cart = new Cart();
            cart.setUserName(userName);
        }
        cart.getItems().add(cartItem);
        return cartRepository.save(cart);
    }

    public void clearCart(String userName) {
        Cart cart = cartRepository.findByUserName(userName);
        if (cart != null) {
            cart.getItems().clear();
            cartRepository.save(cart);
        }
    }

    public void addItemToCart(String userId, String sessionId, Cart.CartItem cartItem) {
        // Check  stock
        /* FIXME: do it really need to check the stock because if this is possible to add to cart
            means this is most likely sent from product view request -> and that request might already done
            checking for availability in inventory
        */
//        if (!productService.isProductInStock(cartItem.getProductId(), cartItem.getQuantity())) {
//            throw new RuntimeException("Product is out of stock or requested quantity exceeds available stock");
//        }

        Cart cart;
        if (userId != null) {
            // registered users -> find cart by userId
            cart = cartRepository.findByUserId(userId)
                    .orElseGet(() -> {
                        Cart newCart = new Cart();
                        newCart.setUserId(userId);
                        return cartRepository.save(newCart);
                    });
        } else if (sessionId != null) {
            // guest -> find cart by sessionId
            cart = cartRepository.findBySessionId(sessionId)
                    .orElseGet(() -> {
                        Cart newCart = new Cart();
                        newCart.setSessionId(sessionId);
                        return cartRepository.save(newCart);
                    });
        } else {
            throw new RuntimeException("Either userId or sessionId must be provided");
        }

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

    public void removeItemFromCart(String userName, String productId) {

        Cart cart = cartRepository.findByUserName(userName);
        if (cart == null)
            throw new RuntimeException("Cart not found");

        // Remove the item
        cart.setItems(cart.getItems().stream()
                .filter(item -> !item.getProductId().equals(productId))
                .toList());

        cartRepository.save(cart);
    }
}
// TODO: handle cartBuilder, using createdAt and UpdatedAt