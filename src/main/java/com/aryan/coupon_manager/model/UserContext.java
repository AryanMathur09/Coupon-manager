package com.aryan.coupon_manager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserContext {
    private String userId;
    private String userTier;
    private String country;
    private Double lifetimeSpend;
    private Integer ordersPlaced;
}
