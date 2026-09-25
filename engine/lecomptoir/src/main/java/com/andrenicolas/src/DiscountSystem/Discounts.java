package com.andrenicolas.src.DiscountSystem;

import java.util.Comparator;
import java.util.List;

import com.andrenicolas.src.Cart;
import com.andrenicolas.src.LoyaltyCard;

public class Discounts {
    private final List<DiscountStrategy> strategies;

    public Discounts() {
        this.strategies = List.of(
            new TenPercentDiscountStrategy(),
            new PointsDiscountStrategy()
        );
    }

    public DiscountResult applyBestDiscount(Cart cart, double currentTotal, LoyaltyCard card) {
        return strategies.stream()
            .map(strategy -> strategy.evaluate(cart, currentTotal, card))
            .max(Comparator.comparingDouble(DiscountResult::discountAmount))
            .orElse(new DiscountResult(currentTotal, 0, "", card));
    }
}