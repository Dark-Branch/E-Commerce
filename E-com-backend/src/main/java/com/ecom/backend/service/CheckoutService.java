package com.ecom.backend.service;

import com.ecom.backend.DTO.CheckoutRequest;
import com.ecom.backend.exception.NotEnoughStockException;
import com.ecom.backend.exception.NotFoundException;
import com.ecom.backend.model.Cart;
import com.ecom.backend.model.Order;
import com.ecom.backend.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CheckoutService {

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;


    public Order checkoutCart(CheckoutRequest request, String userId) {

        Cart cart = cartService.getCartById(request.getCartID());

        if (request.getSelectedItems() == null || request.getSelectedItems().isEmpty()) {
            throw new IllegalArgumentException("No items selected for checkout");
        }

        Date date = new Date();

        Order order = Order.builder()
                .paymentMethod(request.getPaymentMethod())
                .status("Pending")
                .instructions(request.getInstructions())
                .userId(userId)
                .createdAt(date)
                .updatedAt(date)
                .address(request.getAddress())
                .build();

        double totalAmount = 0;

        List<Order.OrderItem> orderItems = new ArrayList<>();

        for (Cart.CartItem selectedItem : request.getSelectedItems()) {
            // Check item is in the cart first - not so needed - for integrity
            Cart.CartItem cartItem = cart.getItems().stream()
                    .filter(item -> item.getProductId().equals(selectedItem.getProductId()))
                    .findFirst()
                    .orElseThrow(() -> new NotFoundException("Product " + selectedItem.getProductId() + " not found in cart"));

            // FIXME: check with cart, needed?
            if (selectedItem.getQuantity() > cartItem.getQuantity()) {
                throw new IllegalArgumentException("Requested quantity for product " + selectedItem.getProductId() + " exceeds available quantity in cart");
            }

            Product product = productService.getProductById(selectedItem.getProductId());

            // available in stock?
            if (product.getInventoryCount() < selectedItem.getQuantity()) {
                throw new NotEnoughStockException("Product " + product.getId() + " is out of stock or quantity exceeds available stock");
            }

            Order.OrderItem orderItem = new Order.OrderItem(
                    selectedItem.getProductId(),
                    selectedItem.getQuantity(),
                    selectedItem.getVersion(),
                    product.getPrice() // Price at the time of order placement
            );
            orderItems.add(orderItem);

            totalAmount += product.getPrice() * selectedItem.getQuantity();

            // FIXME: not doing this here, because this is just initializing order, this step will be after confirming payment
//            product.setInventoryCount(product.getInventoryCount() - selectedItem.getQuantity());
//            productService.updateProduct(product.getId());
        }

        order.setItems(orderItems);
        order.setTotalAmount(totalAmount);

        orderService.createOrder(order);

        // Remove selected items from the cart or update their quantities
        List<Cart.CartItem> remainingItems = new ArrayList<>();
        for (Cart.CartItem cartItem : cart.getItems()) {
            request.getSelectedItems().stream()
                    .filter(selectedItem -> selectedItem.getProductId().equals(cartItem.getProductId()))
                    .findFirst()
                    .ifPresentOrElse(
                            selectedItem -> {
                                // reduce quantity
                                int remainingQuantity = cartItem.getQuantity() - selectedItem.getQuantity();
                                if (remainingQuantity > 0) {
                                    cartItem.setQuantity(remainingQuantity);
                                    remainingItems.add(cartItem);
                                }
                            },
                            () -> remainingItems.add(cartItem) // others left untouched
                    );
        }

        cart.setItems(remainingItems);   // TODO: wht to do to active incative field in cart
        cartService.updateCart(cart);

        return order;
    }
}
// FIXME: is depending on repositories okay or need to access the via service layer
// TODO: add sufficient error handling