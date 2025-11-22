package com.aryan.coupon_manager.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Coupon {
    private String code;
    private String description;
    private String discountType;
    private Double discountValue;
    private Double maxDiscountAmount;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer usageLimitPeruser;
    private Eligibility eligibility;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Eligibility {
        private List<String> allowedUserTiers;
        private Double minLifetimeSpend;
        private Integer minOrdersPlaced;
        private Boolean firstOrderOnly;
        private List<String> allowedCountries;
        private Double minCartValue;
        private List<String> applicableCategories;
        private List<String> excludedCategories;
        private Integer minItemsCount;
    }
}
