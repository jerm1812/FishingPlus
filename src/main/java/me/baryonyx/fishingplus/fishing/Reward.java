package me.baryonyx.fishingplus.fishing;

import org.bukkit.Material;

public class Reward {
    String name;
    String displayName;
    Material item;
    int amount = 1;
    float chance;
    Rarity rarity;
    float price;

    public Reward(String name, String displayName, Material item, int amount, float chance, Rarity rarity, float price) {
        this.name = name;
        this.displayName = displayName;
        this.item = item;
        this.amount = amount;
        this.chance = chance;
        this.rarity = rarity;
        this.price = price;
    }

    public Reward(String name, String displayName, Material item, float chance, Rarity rarity, float price) {
        this.name = name;
        this.displayName = displayName;
        this.item = item;
        this.chance = chance;
        this.rarity = rarity;
        this.price = price;
    }

    public Reward(String name, Material item, int amount, float chance, Rarity rarity, float price) {
        this.name = name;
        this.displayName = name;
        this.item = item;
        this.amount = amount;
        this.chance = chance;
        this.rarity = rarity;
        this.price = price;
    }

    public Reward(String name, Material item, float chance, float price) {
        this.name = name;
        this.displayName = name;
        this.item = item;
        this.chance = chance;
        this.price = price;
    }
}
