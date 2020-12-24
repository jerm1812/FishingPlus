package me.baryonyx.fishingplus.configuration;

import me.baryonyx.fishingplus.FishingPlus;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class RewardConfiguration {
    private FishingPlus plugin;
    private YamlConfiguration config;

    public RewardConfiguration(FishingPlus plugin) {
        this.plugin = plugin;
        loadRewardFile(plugin.getDataFolder());
    }

    private void loadRewardFile(File datafile) {
        File file = new File(datafile, "rewards.yml");
        config = YamlConfiguration.loadConfiguration(file);
    }

    public ConfigurationSection getFishingRewards() {
        return config.getConfigurationSection("fishing-rewards");
    }
    public ConfigurationSection getCompetitionRewards() {
        return config.getConfigurationSection("competition-rewards");
    }
    public ConfigurationSection getModifiers() {
        return config.getConfigurationSection("modifiers");
    }

    public void reload() {
        File file = new File(plugin.getDataFolder(), "rewards.yml");
        if (file.exists()) {
            config = YamlConfiguration.loadConfiguration(file);
        }
    }
}
