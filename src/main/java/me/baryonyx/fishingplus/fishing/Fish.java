package me.baryonyx.fishingplus.fishing;

public class Fish extends Reward {
    public double minLength;
    public double maxLength;

    // A reward that is a fish
    public Fish(String name, double chance, double price, double minLength, double maxLength) {
        super(name, chance, price);
        this.minLength = minLength;
        this.maxLength = maxLength;
    }
}
