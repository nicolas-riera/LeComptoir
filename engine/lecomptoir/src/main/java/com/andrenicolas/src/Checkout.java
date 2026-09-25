package com.andrenicolas.src;

import java.util.EnumSet;
import java.util.Locale;

import com.andrenicolas.src.DiscountSystem.DiscountResult;
import com.andrenicolas.src.DiscountSystem.Discounts;
import com.andrenicolas.src.Enums.Category;

public class Checkout {
    private StringBuilder receipt;
    private final Discounts discounts = new Discounts();
    private final double VAT55Value = 0.055;
    private final double VAT20Value = 0.2;
    private double VAT55;
    private double VAT20;
    private double totalVAT;
    private DiscountResult discountResult;
    private LoyaltyCard updatedCard;

    private void drinkOffer(Cart cart){
        for (double price : cart.getFreeDrinkPrices()){
            this.receipt.append(String.format(Locale.US, "Free drink : -%.2f €\n", price));
            this.VAT55 -= price * VAT55Value;
        }
        this.VAT55 = Math.max(0, this.VAT55); // avoid negative VAT
    }

    private void addTVA(CartLine cartline) {
        if (EnumSet.of(Category.FRESH_FOOD, Category.GROCERY, Category.FROZEN, Category.BAKERY, Category.DRINKS).contains(cartline.getProduct().getCategory())) {
            this.VAT55 += cartline.getSubtotal() * VAT55Value;
        } else {
            this.VAT20 += cartline.getSubtotal() * VAT20Value;
        }
    }

    public void finalPriceCalc(Cart cart, LoyaltyCard card){
        this.VAT55 = 0;
        this.VAT20 = 0;
        this.totalVAT = 0;

        for (CartLine cartline : cart.getCartlines()){
            addTVA(cartline);
        }

        // drinkOffer(cart);

        this.totalVAT = cart.getSubTotal() + this.VAT20 + this.VAT55;
        this.discountResult = discounts.applyBestDiscount(cart, this.totalVAT, card);
        this.totalVAT = discountResult.totalAfterDiscount();
        this.updatedCard = this.discountResult.updatedCard().addPointsFromTotal(this.totalVAT);
    }
    public String showReceipt(Cart cart, LoyaltyCard card) {
        this.receipt = new StringBuilder();

        this.receipt.append("--------RECEIPT-------\n");
        for (CartLine cartline : cart.getCartlines()){

            this.receipt.append(String.format(Locale.US, "%s [%s] x%d : %.2f €\n",
            cartline.getProduct().getLabel(),
            cartline.getProduct().getCategory(),
            cartline.getQuantity(),
            cartline.getSubtotal()));
        }

        // drinkOffer(cart);

        this.receipt.append("------------------------\n");

        this.receipt.append(String.format(Locale.US, "TOTAL VAT 5.5%% : %.2f €\n", this.VAT55));

        this.receipt.append(String.format(Locale.US, "TOTAL VAT 20%% : %.2f €\n", this.VAT20));

        this.receipt.append(this.discountResult.description());

        this.receipt.append(String.format(Locale.US, "TOTAL Inc. VAT : %.2f €\n", this.totalVAT));

        this.receipt.append("------------------------\n");

        this.receipt.append("Loyalty system: \n");

        this.receipt.append(String.format(Locale.US, "Points after this purchase : %d\n", this.updatedCard.getPoints()));

        return this.receipt.toString();
    }

    public String showReceipt(Cart cart) { // If the client doesn't have any loyalty card
        return showReceipt(cart, new LoyaltyCard());
    }
}