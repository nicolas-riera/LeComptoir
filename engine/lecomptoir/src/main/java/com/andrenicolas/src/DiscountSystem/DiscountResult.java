package com.andrenicolas.src.DiscountSystem;

import com.andrenicolas.src.LoyaltyCard;

public record DiscountResult(
    double totalAfterDiscount,
    double discountAmount,
    String description,
    LoyaltyCard updatedCard
) {}