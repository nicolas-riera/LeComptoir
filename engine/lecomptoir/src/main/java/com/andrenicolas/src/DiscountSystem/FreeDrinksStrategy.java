package com.andrenicolas.src.DiscountSystem;

import com.andrenicolas.src.Cart;
import com.andrenicolas.src.CartLine;
import com.andrenicolas.src.LoyaltyCard;
import com.andrenicolas.src.Enums.Category;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class FreeDrinksStrategy implements DiscountStrategy {
    private double allDrinkDiscountPrices;
    @Override
    public DiscountResult evaluate(Cart cart, double currentTotal, LoyaltyCard card) {
        double discountAmount = getFreeDrinkPrices(cart);    

        String descripton = "";
        
        if (discountAmount != 0) {
            descripton = "Free Drink offer applied.\n";
        }
        
        return new DiscountResult(currentTotal - discountAmount, discountAmount, descripton, card);
    }

    public double getFreeDrinkPrices(Cart cart){
        double prices = 0;
        List<Double> drinkPrices = new ArrayList<>();
        for (CartLine cartline : cart.getCartlines()){
            if (cartline.getProduct().getCategory() == Category.DRINKS){
                for (int i = 0; i < cartline.getQuantity(); i++){
                    drinkPrices.add(cartline.getProduct().getUnitPrice());
                }
            }
        }
        Collections.sort(drinkPrices, Collections.reverseOrder());

        for (int i = 2; i < drinkPrices.size(); i = i + 3){
            prices += drinkPrices.get(i);
        }
        return prices;
    }
}
