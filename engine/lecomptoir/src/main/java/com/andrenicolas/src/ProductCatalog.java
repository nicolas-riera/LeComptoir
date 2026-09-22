package com.andrenicolas.src;

import java.util.List;
import com.andrenicolas.src.Enums.Category;

public class ProductCatalog {

    // Initial Product "database"
    public static List<Product> getInitialProducts() {
        return List.of(
            // DRINKS
            new Product("DRK-001", "Mineral Water 1.5L", 0.70, Category.DRINKS),
            new Product("DRK-002", "Pure Orange Juice 1L", 2.10, Category.DRINKS),
            new Product("DRK-003", "Cola Soda 33cl", 1.20, Category.DRINKS),
            new Product("DRK-004", "Semi-Skimmed Milk 1L", 1.05, Category.DRINKS),

            // FRESH_FOOD
            new Product("FSH-001", "Gala Apples (1kg)", 2.50, Category.OTHER),
            new Product("FSH-002", "Whole Chicken 1.2kg", 8.90, Category.OTHER),
            new Product("FSH-003", "Buffalo Mozzarella 125g", 1.85, Category.OTHER),
            new Product("FSH-004", "Fresh Salmon Fillet 200g", 4.50, Category.OTHER),

            // GROCERY
            new Product("GCR-001", "Spaghetti Pasta 500g", 1.15, Category.OTHER),
            new Product("GCR-002", "Basmati Rice 1kg", 2.40, Category.OTHER),
            new Product("GCR-003", "Extra Virgin Olive Oil 75cl", 6.99, Category.OTHER),
            new Product("GCR-004", "Dark Chocolate Bar 100g", 1.60, Category.OTHER),

            // FROZEN
            new Product("FRZ-001", "Four Cheese Frozen Pizza", 3.80, Category.OTHER),
            new Product("FRZ-002", "Frozen Country Vegetables 750g", 2.95, Category.OTHER),

            // HYGIENE & HOME_CARE
            new Product("HYG-001", "Moisturizing Shower Gel 250ml", 2.30, Category.OTHER),
            new Product("HYG-002", "Toothpaste 75ml", 1.90, Category.OTHER),
            new Product("HOM-001", "Lemon Dishwashing Liquid 500ml", 1.45, Category.OTHER),

            // OTHER
            new Product("OTH-001", "AA Batteries (Pack of 4)", 4.20, Category.OTHER)
        );
    }
}