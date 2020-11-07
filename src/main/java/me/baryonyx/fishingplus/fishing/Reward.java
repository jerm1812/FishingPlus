package me.baryonyx.fishingplus.fishing;

import java.util.ArrayList;
import java.util.List;

public class Reward {
    public String name;
    public double chance;
    public List<String> modifiers = new ArrayList<>();
    public List<String> commands;

    // A FishingPlus reward to be stored in a loot table
    public Reward(String name, double chance) {
        this.name = name;
        this.chance = chance;

    }

    public Reward(String name, double chance, List<String> commands) {
        this.name = name;
        this.chance = chance;
        this.commands = commands;
    }

    public Reward(String name) {
        this.name = name;
    }
}
