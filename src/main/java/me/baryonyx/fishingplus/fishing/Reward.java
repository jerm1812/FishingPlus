package me.baryonyx.fishingplus.fishing;

import org.bukkit.Material;

public class Reward {
    public String name;
    public Material item;
    public int amount = 1;
    public float chance;
    public float price;

    public Reward(String name, Material item, int amount, float chance, float price) {
        this.name = name;
        this.item = item;
        this.amount = amount;
        this.chance = chance;
        this.price = price;
    }

    public Reward(String name, Material item, float chance, float price) {
        this.name = name;
        this.item = item;
        this.chance = chance;
        this.price = price;
    }
}
