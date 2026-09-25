package com.andrenicolas.src;

import com.andrenicolas.src.Enums.Category;

public class Product {
    private String reference;
    private String label;
    private double unitPrice;
    private Category category;

    public Product(String reference, String label, double unitPrice, Category category) {
        this.reference = reference;
        this.label = label;
        this.unitPrice = unitPrice;
        this.category = category;
    }

    public String getReference() { return reference; }
    public String getLabel() { return label; }
    public double getUnitPrice() { return unitPrice; }
    public Category getCategory() { return category; }
}

