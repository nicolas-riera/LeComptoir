package com.andrenicolas.src.DiscountSystem;

import java.time.LocalTime;
import java.util.Locale;
import com.andrenicolas.src.Cart;
import com.andrenicolas.src.LoyaltyCard;

public class VipHappyHourDiscountStrategy implements DiscountStrategy {
    private static final LocalTime HAPPY_HOUR_START = LocalTime.of(17, 0);
    private static final LocalTime HAPPY_HOUR_END = LocalTime.of(19, 0);
    private static final double DISCOUNT_RATE = 0.15;
    private static final double MAX_DISCOUNT = 20.0;

    private final LocalTime currentTime;

    public VipHappyHourDiscountStrategy() {
        this(LocalTime.now());
    }

    public VipHappyHourDiscountStrategy(LocalTime currentTime) {
        this.currentTime = currentTime;
    }

    @Override
    public DiscountResult evaluate(Cart cart, double currentTotal, LoyaltyCard card) {
        if (!card.isVip() || currentTime.isBefore(HAPPY_HOUR_START) || currentTime.isAfter(HAPPY_HOUR_END)) {
            return new DiscountResult(currentTotal, 0, "", card);
        }

        double discountAmount = currentTotal * DISCOUNT_RATE;
        double newTotal = Math.max(0, currentTotal - discountAmount);
        String desc = String.format(Locale.US, "VIP Happy Hour (-15%%) discount applied: -%.2f €\n", discountAmount);

        return new DiscountResult(newTotal, discountAmount, desc, card);
    }
}