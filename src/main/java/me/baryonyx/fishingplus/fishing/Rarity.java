package me.baryonyx.fishingplus.fishing;

public class Rarity {
    public String name;
    public String displayName;
    public double chance;
    public double priceModifier;

    public Rarity(String name, String displayName, double chance, double priceModifier) {
        this.name = name;
        this.displayName = displayName;
        this.chance = chance;
        this.priceModifier = priceModifier;
    }

    public Rarity(String name, double chance, double priceModifier) {
        this.name = name;
        this.displayName = name;
        this.chance = chance;
        this.priceModifier = priceModifier;
    }
}
