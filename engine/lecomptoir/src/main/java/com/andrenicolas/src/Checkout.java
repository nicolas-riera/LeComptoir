package com.andrenicolas.src;

// entry point that produces the receipt
public class Checkout {
    private StringBuilder receipt;

    private void drinkOffer(Cart cart){
        for (double price : cart.getFreeDrinkPrices()){
            this.receipt.append(String.format("Free drink : -%.2f €\n", price));
        }
    }

    public String showReceipt(Cart cart){
        this.receipt = new StringBuilder();

        this.receipt.append("--------RECEIPT-------\n");
        for (CartLine cartline : cart.getCartlines()){

            this.receipt.append(String.format("%s [%s] x%d : %.2f €\n",
            cartline.getProduct().getLabel(),
            cartline.getProduct().getCategory(),
            cartline.getQuantity(),
            cartline.getSubtotal()));

        }

        drinkOffer(cart);

        this.receipt.append("------------------------\n");
        this.receipt.append(String.format("TOTAL : %.2f €\n", cart.getSubTotal()));
        if (cart.getSubTotal() > 50) {
            this.receipt.append("10% discount applied.\n");
        }
        return this.receipt.toString();
    }
}
