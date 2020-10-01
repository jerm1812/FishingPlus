package me.baryonyx.fishingplus.fishing;

import org.bukkit.Material;

public class Reward {
    public String name;
    public String displayName;
    public Material item;
    public int amount = 1;
    double chance;
    public Modifier modifier;
    double price;

    public Reward(String name, String displayName, String item, int amount, double chance, Modifier modifier, double price) {
        this.name = name;
        this.displayName = displayName;
        this.item = Material.getMaterial(item);
        this.amount = amount;
        this.chance = chance;
        this.modifier = modifier;
        this.price = price;
    }

    public Reward(String name, String displayName, Material item, double chance, Modifier modifier, double price) {
        this.name = name;
        this.displayName = displayName;
        this.item = item;
        this.chance = chance;
        this.modifier = modifier;
        this.price = price;
    }
    public Reward(String name, String displayName, Material item, double chance, double price) {
        this.name = name;
        this.displayName = displayName;
        this.item = item;
        this.chance = chance;
        this.price = price;
    }

    public Reward(String name, Material item, int amount, double chance, Modifier modifier, double price) {
        this.name = name;
        this.displayName = name;
        this.item = item;
        this.amount = amount;
        this.chance = chance;
        this.modifier = modifier;
        this.price = price;
    }

    public Reward(String name, Material item, double chance, float price) {
        this.name = name;
        this.displayName = name;
        this.item = item;
        this.chance = chance;
        this.price = price;
    }
}
