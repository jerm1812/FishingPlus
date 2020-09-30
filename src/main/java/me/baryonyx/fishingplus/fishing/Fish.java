package me.baryonyx.fishingplus.fishing;


import org.bukkit.Material;

public class Fish extends Reward {
    public float minLength;
    public float maxLength;
    public float actualLength;
    public String caughtBy;

    public Fish(String name, Material item, float chance, Rarity rarity, float price, float minLength, float maxLength) {
        super(name, item, chance, price);
    }
}
