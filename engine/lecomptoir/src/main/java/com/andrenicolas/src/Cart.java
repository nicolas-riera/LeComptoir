package com.andrenicolas.src;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.andrenicolas.src.Enums.Category;

// all the lines and the subtotal calculation
public class Cart {
    private List<CartLine>  cartlines = new ArrayList<>();
    private double          subTotal;

    public void AddCartlines(List<CartLine> cartlines){
        for (CartLine cartline : cartlines){
            this.AddCartline(cartline);
        }
    }

    public void AddCartline(CartLine cartline){
        cartlines.add(cartline);
    }

    public List<Double> getFreeDrinkPrices(){
        List<Double> drinkPrices = new ArrayList<>();
        for (CartLine cartline : this.cartlines){
            if (cartline.getProduct().getCategory() == Category.DRINKS){
                for (int i = 0; i < cartline.getQuantity(); i++){
                    drinkPrices.add(cartline.getProduct().getUnitPrice());
                }
            }
        }
        Collections.sort(drinkPrices, Collections.reverseOrder());

        List<Double> freeDrinks = new ArrayList<>();
        for (int i = 2; i < drinkPrices.size(); i = i + 3){
            freeDrinks.add(drinkPrices.get(i));
        }
        return freeDrinks;
    }

    private void calcSubTotal(){
        this.subTotal = 0;
        for (CartLine cartline : this.cartlines){
            this.subTotal = this.subTotal + cartline.getSubtotal();
        }
        for (double price : this.getFreeDrinkPrices()){
            this.subTotal = this.subTotal - price;
        }
    }

    public double getSubTotal(){
        this.calcSubTotal();
        return this.subTotal;
    }

    public List<CartLine> getCartlines(){
        return this.cartlines;
    }

    public void clear() {
        this.cartlines.clear();
        this.subTotal = 0;
    }

}
