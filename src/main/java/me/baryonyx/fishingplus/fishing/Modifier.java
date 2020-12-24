package me.baryonyx.fishingplus.fishing;

import java.util.List;

public class Modifier {
    public String name;
    public String displayName;
    public double chance;
    public double priceModifier;
    public List<String> fish;

    // Modifier for a FishingPlus reward
    public Modifier(String name, String displayName, double priceModifier, double chance, List<String> fish) {
        this.name = name;
        this.displayName = displayName;
        this.chance = chance;
        this.priceModifier = priceModifier;
        this.fish = fish;
    }
}
