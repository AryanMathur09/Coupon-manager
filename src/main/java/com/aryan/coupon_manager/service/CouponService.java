package com.aryan.coupon_manager.service;

import com.aryan.coupon_manager.model.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CouponService {

    private final List<Coupon> couponDatabase = new ArrayList<>();

    public void saveCoupon(Coupon coupon) {
        couponDatabase.removeIf(c -> c.getCode().equalsIgnoreCase(coupon.getCode()));
        couponDatabase.add(coupon);
    }

    public List<Coupon> getAllCoupons() {
        return couponDatabase;
    }

    public Map<String, Object> getBestCoupon(UserContext user, Cart cart) {
        LocalDate now = LocalDate.now();

        // 1. Filter coupons that are valid
        List<Coupon> eligibleCoupons = couponDatabase.stream()
                .filter(c -> isDateValid(c, now))
                // .filter(c -> isUsageLimitValid(c, user))
                .filter(c -> isEligible(c, user, cart))
                .collect(Collectors.toList());

        if (eligibleCoupons.isEmpty()) {
            return null;
        }

        Map<Coupon, Double> couponDiscounts = new HashMap<>();
        for (Coupon c : eligibleCoupons) {
            couponDiscounts.put(c, calculateDiscount(c, cart));
        }

        Coupon bestCoupon = eligibleCoupons.stream()
                .sorted((c1, c2) -> {
                    Double d1 = couponDiscounts.get(c1);
                    Double d2 = couponDiscounts.get(c2);

                    int discountCompare = d2.compareTo(d1);
                    if (discountCompare != 0) return discountCompare;

                    LocalDate date1 = c1.getEndDate();
                    LocalDate date2 = c2.getEndDate();

                    if (date1 == null && date2 == null) return 0;
                    if (date1 == null) return 1;
                    if (date2 == null) return -1;

                    int dateCompare = date1.compareTo(date2);
                    if (dateCompare != 0) return dateCompare;

                    // 3. Lexicographically smaller code (Ascending)
                    return c1.getCode().compareTo(c2.getCode());
                })
                .findFirst()
                .orElse(null);

        if (bestCoupon == null) return null;

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("coupon", bestCoupon);
        response.put("discountAmount", couponDiscounts.get(bestCoupon));
        return response;
    }


    private boolean isDateValid(Coupon c, LocalDate now) {
        if (c.getStartDate() != null && now.isBefore(c.getStartDate())) return false;
        if (c.getEndDate() != null && now.isAfter(c.getEndDate())) return false;
        return true;
    }

    private double calculateDiscount(Coupon c, Cart cart) {
        double totalCartValue = cart.getTotalPrice();
        double discount = 0.0;

        if ("FLAT".equalsIgnoreCase(c.getDiscountType())) {
            discount = c.getDiscountValue();
        } else if ("PERCENT".equalsIgnoreCase(c.getDiscountType())) {
            discount = (c.getDiscountValue() / 100.0) * totalCartValue;
            if (c.getMaxDiscountAmount() != null) {
                discount = Math.min(discount, c.getMaxDiscountAmount());
            }
        }
        return discount;
    }

    private boolean isEligible(Coupon c, UserContext user, Cart cart) {
        Coupon.Eligibility rules = c.getEligibility();
        if (rules == null) return true;

        if (rules.getAllowedUserTiers() != null && !rules.getAllowedUserTiers().isEmpty()) {
            if (!rules.getAllowedUserTiers().contains(user.getUserTier())) return false;
        }
        if (rules.getAllowedCountries() != null && !rules.getAllowedCountries().isEmpty()) {
            if (!rules.getAllowedCountries().contains(user.getCountry())) return false;
        }
        if (rules.getMinLifetimeSpend() != null && user.getLifetimeSpend() < rules.getMinLifetimeSpend()) {
            return false;
        }
        if (rules.getMinOrdersPlaced() != null && user.getOrdersPlaced() < rules.getMinOrdersPlaced()) {
            return false;
        }
        if (Boolean.TRUE.equals(rules.getFirstOrderOnly()) && user.getOrdersPlaced() > 0) {
            return false;
        }

        if (rules.getMinCartValue() != null && cart.getTotalPrice() < rules.getMinCartValue()) {
            return false;
        }
        if (rules.getMinItemsCount() != null && cart.getTotalQuantity() < rules.getMinItemsCount()) {
            return false;
        }

        if (rules.getExcludedCategories() != null && !rules.getExcludedCategories().isEmpty()) {
            boolean hasExcluded = cart.getItems().stream()
                    .anyMatch(item -> rules.getExcludedCategories().contains(item.getCategory()));
            if (hasExcluded) return false;
        }
        if (rules.getApplicableCategories() != null && !rules.getApplicableCategories().isEmpty()) {
            boolean hasApplicable = cart.getItems().stream()
                    .anyMatch(item -> rules.getApplicableCategories().contains(item.getCategory()));
            if (!hasApplicable) return false;
        }

        return true;
    }
}