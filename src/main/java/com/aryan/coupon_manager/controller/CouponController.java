package com.aryan.coupon_manager.controller;

import com.aryan.coupon_manager.model.*;
import com.aryan.coupon_manager.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
public class CouponController {

    @Autowired
    private CouponService couponService;

    @PostMapping("/coupons")
    public ResponseEntity<String> createCoupon(@RequestBody Coupon coupon) {
        couponService.saveCoupon(coupon);
        return ResponseEntity.ok("Coupon Created");
    }
    @PostMapping("/applicable-coupons")
    public ResponseEntity<Object> getBestCoupon(@RequestBody CouponRequest request) {
        Map<String, Object> result = couponService.getBestCoupon(request.getUser(), request.getCart());
        if (result == null) {
            return ResponseEntity.ok().body("No coupon available for this cart.");
        }
        return ResponseEntity.ok(result);
    }
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        if ("hire-me@anshumat.org".equals(email) && "HireMe@2025!".equals(password)) {
            return ResponseEntity.ok("Login Successful");
        }
        return ResponseEntity.status(401).body("Invalid Credentials");
    }
}