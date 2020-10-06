package me.baryonyx.fishingplus.fishing;

import org.bukkit.Material;

import java.util.List;

public class Reward {
    public String name;
    public String displayName;
    public Material item;
    public int amount;
    public double chance;
    public Modifier modifier;
    public double price;
    public List<String> lore;

    public Reward(String name, String displayName, Material item, double chance, double price, List<String> lore, int amount) {
        this.name = name;
        this.displayName = displayName;
        this.item = item;
        this.chance = chance;
        this.price = price;
        this.lore = lore;
        this.amount = amount;
    }

    public Reward(String name, double price) {
        this.name = name;
        this.price = price;
    }

    //FIXME implement a separate "shop reward" to for just the name, price, and modifier
}
