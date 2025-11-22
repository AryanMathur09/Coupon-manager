package com.aryan.coupon_manager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Cart {
private List<CartItem> items;
    public Double getTotalPrice() {
        if (items == null || items.isEmpty()) return 0.0;
        return items.stream()
                .mapToDouble(item -> item.getUnitPrice() * item.getQuantity())
                .sum();
    }

    public Integer getTotalQuantity() {
        if (items == null || items.isEmpty()) return 0;
        return items.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }
}
