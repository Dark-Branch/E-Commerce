package com.ecom.backend.dto;

import com.ecom.backend.model.Cart;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CheckoutRequest {
    private String cartID;
    private String address;
    private String paymentMethod;
    private String instructions;
    private List<Cart.CartItem> selectedItems;
}
