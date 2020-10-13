package me.baryonyx.fishingplus.fishing;

public class Fish extends Reward {
    public double minLength;
    public double maxLength;
    public double actualLength;

    // A reward that is a fish
    public Fish(String name, double chance, double price, double minLength, double maxLength) {
        super(name, chance, price);
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    public Fish(String name, double actualLength) {
        super(name);
        this.actualLength = actualLength;
    }
}
