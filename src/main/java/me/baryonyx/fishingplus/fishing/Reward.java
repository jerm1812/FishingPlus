package me.baryonyx.fishingplus.fishing;

import org.bukkit.WeatherType;
import org.bukkit.block.Biome;

import java.util.ArrayList;
import java.util.List;

public class Reward {
    public String name;
    public String displayName;
    public double chance;
    public int customModelData;
    public List<Biome> biomes = new ArrayList<>();
    public WeatherType weather;
    public List<String> modifiers = new ArrayList<>();
    public List<String> commands;

    public Reward(String name, double chance, int customModelData, List<String> commands) {
        this.name = name;
        this.chance = chance;
        this.customModelData = customModelData;
        this.commands = commands;
    }

    public Reward(String name, String displayName, double chance, List<String> commands) {
        this.name = name;
        this.displayName = displayName;
        this.chance = chance;
        this.commands = commands;
    }

    public Reward(String name, double chance, int customModelData, List<String> commands, WeatherType weather) {
        this.name = name;
        this.chance = chance;
        this.customModelData = customModelData;
        this.commands = commands;
        this.weather = weather;
    }

    public Reward(String name) {
        this.name = name;
    }
}
