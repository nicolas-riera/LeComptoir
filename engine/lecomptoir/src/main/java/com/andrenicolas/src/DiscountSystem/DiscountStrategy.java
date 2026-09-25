package com.andrenicolas.src.DiscountSystem;

import com.andrenicolas.src.Cart;
import com.andrenicolas.src.LoyaltyCard;

public interface DiscountStrategy {
    DiscountResult evaluate(Cart cart, double currentTotal, LoyaltyCard card);
}