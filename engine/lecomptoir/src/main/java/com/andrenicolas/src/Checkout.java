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

        double originalTotal = cart.getSubTotal() + this.VAT20 + this.VAT55;
        this.discountResult = discounts.applyBestDiscount(cart, originalTotal, card);

        if (originalTotal > 0) {
            double ratio = this.discountResult.totalAfterDiscount() / originalTotal;
            this.VAT55 *= ratio;
            this.VAT20 *= ratio;
        }

        this.totalVAT = this.discountResult.totalAfterDiscount();
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