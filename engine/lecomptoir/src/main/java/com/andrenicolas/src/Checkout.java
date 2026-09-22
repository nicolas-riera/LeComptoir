package com.andrenicolas.src;


// entry point that produces the receipt
public class Checkout {
    public String showReceipt(Cart cart){
        StringBuilder receipt = new StringBuilder();

        receipt.append("--------RECEIPT-------\n");
        for (CartLine cartline : cart.getCartlines()){
              receipt.append(String.format("%s [%s] x%d : %.2f €\n",
              cartline.getProduct().getLabel(),
              cartline.getProduct().getCategory(),
              cartline.getQuantity(),
              cartline.getSubtotal()));
        }
        receipt.append("------------------------\n");
        receipt.append(String.format("TOTAL : %.2f €\n", cart.getSubTotal()));
        if (cart.getSubTotal() > 50) {
            receipt.append("10% discount applied.\n");
        }
        return receipt.toString();
    }
}
