package com.andrenicolas.src;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.EnumSet;
import java.util.Locale;

import com.andrenicolas.src.Enums.Category;

// entry point that produces the receipt
public class Checkout {
    private StringBuilder receipt;
    private double VAT55;
    private double VAT20;

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

    public void savePointsFile(CartLine cartLine) throws IOException{
        String data_to_save = Double.toString(cartline.getSubTotal() + this.loadPointsFile());
        Files.writeString(Paths.get("data.txt"), data_to_save);
    }

    public Double loadPointsFile() throws IOException{
        return Double.valueOf(Files.readString(Paths.get("data.txt")));
    }

    public String showReceipt(Cart cart){
        this.receipt = new StringBuilder();
        this.VAT55 = 0;
        this.VAT20 = 0;

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

        double totalVAT = cart.getSubTotal() + this.VAT20 + this.VAT55;
        if (cart.getSubTotal() > 50) {
            this.receipt.append("10% discount applied.\n");
            totalVAT *= 0.9;
        }
        this.receipt.append(String.format(Locale.US, "TOTAL Inc. VAT : %.2f €\n", totalVAT));
        return this.receipt.toString();
    }
}
