package com.aryan.coupon_manager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class CouponRequest {
    private UserContext user;
    private Cart cart;
}
