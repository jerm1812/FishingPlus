package me.baryonyx.fishingplus.configuration;

import me.baryonyx.fishingplus.FishingPlus;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class RewardConfiguration {
    private YamlConfiguration config;

    public RewardConfiguration(FishingPlus plugin) {
        loadRewardFile(plugin.getDataFolder());
    }

    private void loadRewardFile(File datafile) {
        File file = new File(datafile, "rewards.yml");
        config = YamlConfiguration.loadConfiguration(file);
    }

    public ConfigurationSection getRewards() {
        return config.getConfigurationSection("rewards");
    }
    public ConfigurationSection getModifiers() {
        return config.getConfigurationSection("modifiers");
    }
}
