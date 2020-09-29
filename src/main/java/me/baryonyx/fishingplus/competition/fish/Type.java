package me.baryonyx.fishingplus.competition.fish;

import org.bukkit.Material;

import static java.lang.Math.random;

public class Type {
    public String name;
    public float maxLength;
    public float minLength;
    public Material item;
    public float price;

    public Type(String name, float maxLength, float minLength, Material item, float price) {
        this.name = name; this.maxLength = maxLength; this.minLength = minLength; this.item = item; this.price = price;
    }

    // Creates a
    private double generateLength() {
        return Math.round((minLength + (maxLength + 1 - minLength) * Math.pow(random(), 1.35))* 100) / 100;
    }
}
