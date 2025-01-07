package com.ecom.backend.service;

import com.ecom.backend.DTO.CheckoutRequest;
import com.ecom.backend.exception.ProductNotFoundException;
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

    public Order checkoutCart(CheckoutRequest request) {
        Cart cart = cartService.getCartById(request.getCartID());

        Date date = new Date();

        Order order = Order.builder().paymentMethod(request.getPaymentMethod())
                .status("Pending")
                .instructions(request.getInstructions())
                .userId(cart.getUserId()) // use token later
                .createdAt(date)
                .updatedAt(date)
                .address(request.getAddress())
                .build();

        double totalAmount = 0;

        List<Order.OrderItem> orderItems = new ArrayList<>();
        for (Cart.CartItem cartItem : cart.getItems()) {
            Product product = productService.getProductById(cartItem.getProductId());
            // FIXME: do i need to add product id in error message

            Order.OrderItem orderItem = new Order.OrderItem(
                    cartItem.getProductId(),
                    cartItem.getQuantity(),  // TODO: add reduce inventory logic
                    cartItem.getVersion(),
                    product.getPrice()      //  price at the time of order placement
            );
            orderItems.add(orderItem);

            totalAmount += product.getPrice() * cartItem.getQuantity();
        }

        order.setItems(orderItems);
        order.setTotalAmount(totalAmount);

        orderService.createOrder(order);

        cartService.deleteCart(cart);

        return order;
    }
}
// FIXME: is depending on repositories okay or need to access the via service layer
// TODO: add sufficient error handling