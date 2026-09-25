package com.andrenicolas.src.DiscountSystem;

import java.util.Comparator;
import java.util.List;

import com.andrenicolas.src.Cart;
import com.andrenicolas.src.LoyaltyCard;

public class Discounts {
    private static final double VIP_LOYALTY_CAP = 20.0;
    
    private final List<DiscountStrategy> mandatoryStrategies;
    private final List<DiscountStrategy> optionalStrategies;
    private final VipHappyHourDiscountStrategy vipHappyHourStrategy;

    public Discounts() {
        this.mandatoryStrategies = List.of(
            new FreeDrinksStrategy()
        );
        this.optionalStrategies = List.of(
            new TenPercentDiscountStrategy(),
            new PointsDiscountStrategy()
        );
        this.vipHappyHourStrategy = new VipHappyHourDiscountStrategy();
    }

    public DiscountResult applyBestDiscount(Cart cart, double currentTotal, LoyaltyCard card) {
        double runningTotal = currentTotal;
        double mandatoryDiscountAmount = 0.0;
        StringBuilder mandatoryDescription = new StringBuilder();
        LoyaltyCard currentCard = card;

        for (DiscountStrategy strategy : mandatoryStrategies) {
            DiscountResult result = strategy.evaluate(cart, runningTotal, currentCard);
            runningTotal = result.totalAfterDiscount();
            mandatoryDiscountAmount += result.discountAmount();
            mandatoryDescription.append(result.description());
            currentCard = result.updatedCard();
        }

        final double totalAfterMandatory = runningTotal;
        final LoyaltyCard cardAfterMandatory = currentCard;

        DiscountResult optionOnlyResult = optionalStrategies.stream()
            .map(strategy -> strategy.evaluate(cart, totalAfterMandatory, cardAfterMandatory))
            .max(Comparator.comparingDouble(DiscountResult::discountAmount))
            .orElse(new DiscountResult(totalAfterMandatory, 0, "", cardAfterMandatory));

        DiscountResult vipResult = vipHappyHourStrategy.evaluate(cart, totalAfterMandatory, cardAfterMandatory);
        double vipDiscount = vipResult.discountAmount();

        if (vipDiscount > 0) {
            double totalAfterVip = vipResult.totalAfterDiscount();
            double remainingCap = Math.max(0, VIP_LOYALTY_CAP - vipDiscount);
            double effectiveTotalForOptional = Math.min(totalAfterVip, remainingCap);

            DiscountResult cappedOptionalResult = optionalStrategies.stream()
                .map(strategy -> strategy.evaluate(cart, effectiveTotalForOptional, vipResult.updatedCard()))
                .max(Comparator.comparingDouble(DiscountResult::discountAmount))
                .orElse(new DiscountResult(totalAfterVip, 0, "", vipResult.updatedCard()));

            double comboDiscount = vipDiscount + cappedOptionalResult.discountAmount();

            if (comboDiscount >= optionOnlyResult.discountAmount()) {
                double finalTotal = totalAfterMandatory - comboDiscount;
                double totalDiscount = mandatoryDiscountAmount + comboDiscount;
                String combinedDesc = mandatoryDescription.toString() + vipResult.description() + cappedOptionalResult.description();

                return new DiscountResult(finalTotal, totalDiscount, combinedDesc, cappedOptionalResult.updatedCard());
            }
        }

        double finalTotal = optionOnlyResult.totalAfterDiscount();
        double totalDiscount = mandatoryDiscountAmount + optionOnlyResult.discountAmount();
        String combinedDesc = mandatoryDescription.toString() + optionOnlyResult.description();

        return new DiscountResult(finalTotal, totalDiscount, combinedDesc, optionOnlyResult.updatedCard());
    }
}