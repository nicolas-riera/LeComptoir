package com.andrenicolas.src.DiscountSystem;

import java.util.Comparator;
import java.util.List;

import com.andrenicolas.src.Cart;
import com.andrenicolas.src.LoyaltyCard;

public class Discounts {
    private final List<DiscountStrategy> mandatoryStrategies;
    private final List<DiscountStrategy> optionalStrategies;

    public Discounts() {
        this.mandatoryStrategies = List.of(
            new FreeDrinksStrategy()
        );
        this.optionalStrategies = List.of(
            new TenPercentDiscountStrategy(),
            new PointsDiscountStrategy()
        );
    }

    public DiscountResult applyBestDiscount(Cart cart, double currentTotal, LoyaltyCard card) {
        double runningTotal = currentTotal;
        double mandatoryDiscountAmount = 0.0;
        StringBuilder combinedDescription = new StringBuilder();
        LoyaltyCard currentCard = card;

        for (DiscountStrategy strategy : mandatoryStrategies) {
            DiscountResult result = strategy.evaluate(cart, runningTotal, currentCard);
            runningTotal = result.totalAfterDiscount();
            mandatoryDiscountAmount += result.discountAmount();
            combinedDescription.append(result.description());
            currentCard = result.updatedCard();
        }

        final double totalAfterMandatory = runningTotal;
        final LoyaltyCard cardAfterMandatory = currentCard;

        DiscountResult bestOptionalResult = optionalStrategies.stream()
            .map(strategy -> strategy.evaluate(cart, totalAfterMandatory, cardAfterMandatory))
            .max(Comparator.comparingDouble(DiscountResult::discountAmount))
            .orElse(new DiscountResult(totalAfterMandatory, 0, "", cardAfterMandatory));

        double finalTotal = bestOptionalResult.totalAfterDiscount();
        double totalDiscountAmount = mandatoryDiscountAmount + bestOptionalResult.discountAmount();
        combinedDescription.append(bestOptionalResult.description());

        return new DiscountResult(finalTotal, totalDiscountAmount, combinedDescription.toString(), bestOptionalResult.updatedCard());
    }
}