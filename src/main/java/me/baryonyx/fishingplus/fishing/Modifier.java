package me.baryonyx.fishingplus.fishing;

public class Modifier {
    public String name;
    public String displayName;
    public double chance;
    public double priceModifier;

    public Modifier(String name, String displayName, double chance, double priceModifier) {
        this.name = name;
        this.displayName = displayName;
        this.chance = chance;
        this.priceModifier = priceModifier;
    }
}
