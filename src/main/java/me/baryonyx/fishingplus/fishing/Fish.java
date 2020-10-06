package me.baryonyx.fishingplus.fishing;

import org.bukkit.Material;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Fish extends Reward {
    public double minLength;
    public double maxLength;
    public double actualLength;
    public String caughtBy;

    public Fish(String name, String displayName, Material item, double chance, double price,
                double minLength, double maxLength, @Nullable List<String> lore, int amount) {
        super(name, displayName, item, chance, price, lore, amount);
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    public Fish(String name, double price) {
        super(name, price);
    }

    //FIXME create a "caught fish" that has a name, price, modifier, length
}
