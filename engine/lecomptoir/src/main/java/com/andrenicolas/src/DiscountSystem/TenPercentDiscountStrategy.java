package com.andrenicolas.src.DiscountSystem;

import com.andrenicolas.src.Cart;
import com.andrenicolas.src.LoyaltyCard;

public class TenPercentDiscountStrategy implements DiscountStrategy {
    @Override
    public DiscountResult evaluate(Cart cart, double currentTotal, LoyaltyCard card) {
        if (cart.getSubTotal() <= 50) {
            return new DiscountResult(currentTotal, 0, "", card);
        }

        double discountAmount = currentTotal * 0.10;
        double newTotal = currentTotal - discountAmount;
        return new DiscountResult(newTotal, discountAmount, "10% discount applied.\n", card);
    }
}