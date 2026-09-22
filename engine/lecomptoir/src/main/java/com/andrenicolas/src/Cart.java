package com.andrenicolas.src;
import java.util.ArrayList;
import java.util.List;

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

    private void calcSubTotal(){
        this.subTotal = 0;
        for (CartLine cartline : this.cartlines){
            this.subTotal = this.subTotal + cartline.getSubtotal();
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
