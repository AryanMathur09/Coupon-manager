package com.aryan.coupon_manager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class CartItem {
    private String productId;
    private String category;
    private  Double unitPrice;
    private Integer quantity;
}
