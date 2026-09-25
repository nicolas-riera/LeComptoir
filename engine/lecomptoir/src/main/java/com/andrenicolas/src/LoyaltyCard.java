package com.andrenicolas.src;

public class LoyaltyCard {
    private final int points;
    private final boolean isVip;

    // Constructors
    public LoyaltyCard(int points, boolean isVip) { // Points and VIP
        this.points = Math.max(0, points);
        this.isVip = isVip;
    }
    public LoyaltyCard(int points) { // Points only
        this(points, false);
    }
    public LoyaltyCard() { // Nothing specified
        this(0, false);
    }

    public int getPoints() {
        return points;
    }

    public boolean isVip() {
        return isVip;
    }

    public double calculateDiscount() {
        int nbPointsDiscounts = points / 100;
        return nbPointsDiscounts * 5.0;
    }

    public LoyaltyCard addPointsFromTotal(double spentAmount) {
        int earnedPoints = (int) spentAmount;
        return new LoyaltyCard(this.points + earnedPoints, this.isVip);
    }

    public LoyaltyCard deductUsedPoints(double discountApplied) {
        int usedPoints = ((int) (discountApplied / 5.0)) * 100;
        int remainingPoints = Math.max(0, this.points - usedPoints);
        return new LoyaltyCard(remainingPoints, this.isVip);
    }
}