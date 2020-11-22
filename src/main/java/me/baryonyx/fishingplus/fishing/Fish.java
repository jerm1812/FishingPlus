package me.baryonyx.fishingplus.fishing;

import java.util.List;

public class Fish extends Reward {
    public double minLength;
    public double maxLength;
    public double actualLength;

    // A reward that is a fish
    public Fish(String name, double chance, int customModelData, List<String> commands, double minLength, double maxLength) {
        super(name, chance, customModelData, commands);
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    public Fish(String name, double actualLength) {
        super(name);
        this.actualLength = actualLength;
    }
}
