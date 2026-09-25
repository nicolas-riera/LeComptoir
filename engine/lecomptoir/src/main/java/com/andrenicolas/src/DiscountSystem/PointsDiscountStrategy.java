package com.andrenicolas.src.DiscountSystem;

import com.andrenicolas.src.Cart;
import com.andrenicolas.src.LoyaltyCard;

import java.util.Locale;

public class PointsDiscountStrategy implements DiscountStrategy {
    @Override
    public DiscountResult evaluate(Cart cart, double currentTotal, LoyaltyCard card) {
        int availableTranches = card.getPoints() / 100;
        if (availableTranches <= 0) {
            return new DiscountResult(currentTotal, 0, "", card);
        }

        int neededTranches = (int) Math.ceil(currentTotal / 5.0);
        int tranchesToUse = Math.min(availableTranches, neededTranches);
        double maxPointsDiscount = tranchesToUse * 5.0;
        double actualDiscount = Math.min(currentTotal, maxPointsDiscount);
        double newTotal = currentTotal - actualDiscount;

        String description = String.format(
            Locale.US,
            "Points (-%.2f €) discount applied, %d points have been used.\n",
            actualDiscount,
            (100 * tranchesToUse)
        );

        LoyaltyCard updatedCard = card.deductUsedPoints(actualDiscount);
        return new DiscountResult(newTotal, actualDiscount, description, updatedCard);
    }
}