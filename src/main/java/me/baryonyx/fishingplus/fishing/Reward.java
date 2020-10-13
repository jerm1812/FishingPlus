package me.baryonyx.fishingplus.fishing;

public class Reward {
    public String name;
    public double chance;
    public double price;

    // A standard reward to be stored in a loot table
    public Reward(String name, double chance, double price) {
        this.name = name;
        this.chance = chance;
        this.price = price;
    }

    public Reward(String name) {
        this.name = name;
    }
}
