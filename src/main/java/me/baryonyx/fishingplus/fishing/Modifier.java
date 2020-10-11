package me.baryonyx.fishingplus.fishing;

public class Modifier {
    public String name;
    public String displayName;
    public double chance;
    public double priceModifier;

    // Modifier for a FishingPlus reward
    public Modifier(String name, String displayName, double priceModifier, double chance) {
        this.name = name;
        this.displayName = displayName;
        this.chance = chance;
        this.priceModifier = priceModifier;
    }
}
