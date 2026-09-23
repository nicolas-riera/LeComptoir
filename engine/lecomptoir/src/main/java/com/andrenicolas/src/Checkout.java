package com.andrenicolas.src;

import java.util.EnumSet;
import java.util.Locale;

import com.andrenicolas.src.Enums.Category;

// entry point that produces the receipt
public class Checkout {
    private StringBuilder receipt;
    private double VAT55;
    private double VAT20;
    private double totalVAT;

    private void drinkOffer(Cart cart){
        for (double price : cart.getFreeDrinkPrices()){
            this.receipt.append(String.format(Locale.US, "Free drink : -%.2f €\n", price));
            this.VAT55 -= price * 0.055;
        }
        this.VAT55 = Math.max(0, this.VAT55); // avoid negative VAT
    }

    private void addTVA(CartLine cartline) {
        if (EnumSet.of(Category.FRESH_FOOD, Category.GROCERY, Category.FROZEN, Category.BAKERY, Category.DRINKS).contains(cartline.getProduct().getCategory())) {
            this.VAT55 += cartline.getSubtotal() * 0.055;
        } else {
            this.VAT20 += cartline.getSubtotal() * 0.2;
        }
    }

    private void discountChecks(Cart cart) {
        double temp10discount = -1;
        double tempPointsdiscount = -1;

        if (cart.getSubTotal() > 50) {
            temp10discount = this.totalVAT * 0.9;
        }

        int nbPointsDiscounts = PointsManagement.loadPoints() / 100;
        tempPointsdiscount = Math.max(0, this.totalVAT - (5 * nbPointsDiscounts));

        if (temp10discount != -1 && (nbPointsDiscounts == 0 || temp10discount <= tempPointsdiscount)) {
            this.receipt.append("10% discount applied.\n");
            this.totalVAT = temp10discount;
        } else if (nbPointsDiscounts > 0) {
            this.totalVAT = tempPointsdiscount;
            this.receipt.append(String.format(Locale.US, "Points (-%d €) discount applied, %d points have been used.\n", (5*nbPointsDiscounts), (100*nbPointsDiscounts)));
            PointsManagement.removePointsByNumber(nbPointsDiscounts);
        }

    }

    public String showReceipt(Cart cart){
        this.receipt = new StringBuilder();
        this.VAT55 = 0;
        this.VAT20 = 0;
        this.totalVAT = 0;

        this.receipt.append("--------RECEIPT-------\n");
        for (CartLine cartline : cart.getCartlines()){

            this.receipt.append(String.format(Locale.US, "%s [%s] x%d : %.2f €\n",
            cartline.getProduct().getLabel(),
            cartline.getProduct().getCategory(),
            cartline.getQuantity(),
            cartline.getSubtotal()));

            addTVA(cartline);

        }

        drinkOffer(cart);

        this.receipt.append("------------------------\n");

        this.receipt.append(String.format(Locale.US, "TOTAL VAT 5.5%% : %.2f €\n", this.VAT55));
        this.receipt.append(String.format(Locale.US, "TOTAL VAT 20%% : %.2f €\n", this.VAT20));

        this.totalVAT = cart.getSubTotal() + this.VAT20 + this.VAT55;
        
        discountChecks(cart); 

        this.receipt.append(String.format(Locale.US, "TOTAL Inc. VAT : %.2f €\n", this.totalVAT));

        this.receipt.append("------------------------\n");

        this.receipt.append("Loyalty system: \n");
        PointsManagement.addPointsFromTotal(totalVAT);
        this.receipt.append(String.format(Locale.US, "Points after this purchase : %d\n", PointsManagement.loadPoints()));

        return this.receipt.toString();
    }
}
