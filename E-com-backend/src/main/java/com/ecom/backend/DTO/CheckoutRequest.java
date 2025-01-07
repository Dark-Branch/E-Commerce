package com.ecom.backend.DTO;

import com.ecom.backend.model.Cart;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class CheckoutRequest {
    private String cartID;
    private String address;
    private String paymentMethod;
    private String instructions;
}
