package me.baryonyx.fishingplus.fishing;

public class CaughtReward {
    public String name;
    public Modifier modifier;
    public String caughtBy;
    public double price;

    public CaughtReward(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public CaughtReward() {

    }
}
