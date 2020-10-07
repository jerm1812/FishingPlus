package me.baryonyx.fishingplus.configuration;

import me.baryonyx.fishingplus.FishingPlus;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

public class Config {
    private FishingPlus plugin;
    private FileConfiguration config;

    public Config(@NotNull FishingPlus plugin) {
        this.plugin = plugin;
        config = plugin.getConfig();
    }

    public double getFishLengthWeight() {
        return config.getDouble("fish-length-weight");
    }

    public boolean getDisplayWhoCaught() {
        return config.getBoolean("display-who-caught");
    }

    public String getRewardPrefix() {
        return config.getString("reward-prefix");
    }

    public String getRewardSuffix() {
        return config.getString("reward-suffix");
    }

    public boolean getAllowModifiers() {
        return config.getBoolean("enable-modifiers");
    }

    public String getModifierPrefix() {
        return config.getString("modifier-prefix");
    }

    public String getModifierSuffix() {
        return config.getString("modifier-suffix");
    }

    public boolean getModifierAddition() {
        return config.getBoolean("price-increase-addition");
    }
}
