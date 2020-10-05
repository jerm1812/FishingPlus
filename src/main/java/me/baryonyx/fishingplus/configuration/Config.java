package me.baryonyx.fishingplus.configuration;

import me.baryonyx.fishingplus.FishingPlus;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;


public class Config {
    private final File datafile;
    private FishingPlus plugin;
    private YamlConfiguration rewardConfig;
    private YamlConfiguration config;

    public Config(FishingPlus plugin) {
        this.plugin = plugin;
        datafile = this.plugin.getDataFolder();
        config = (YamlConfiguration) plugin.getConfig();
        loadRewardFile();
    }

    private void loadRewardFile() {
        File file = new File(datafile, "rewards.yml");
        rewardConfig = YamlConfiguration.loadConfiguration(file);
    }

    private void createRewardConfig() {
        rewardConfig = new YamlConfiguration();
        rewardConfig.createSection("fish-types");
        saveFishTypes(rewardConfig);
    }

    public void addFishType(String name, float minLength, float maxLength, Material item, float price) {
        rewardConfig.set("rewards." + name + ".price", price);
        rewardConfig.set("rewards." + name + ".maxLength", maxLength);
        rewardConfig.set("rewards." + name + ".minLength", minLength);
        rewardConfig.set("rewards." + name + ".item", item);
    }

    private void saveFishTypes(YamlConfiguration config) {
        File file = new File(datafile, "rewards.yml");
        try {
            config.save(file);
        } catch (IOException e) {
            Bukkit.getLogger().severe("Could not save fish types");
            Bukkit.getLogger().severe(e.getMessage());
        }
    }

    public ConfigurationSection getRewards() {
        return rewardConfig.getConfigurationSection("rewards");
    }

    public ConfigurationSection getModifiers() {
        return rewardConfig.getConfigurationSection("modifiers");
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
        return config.getBoolean("allow-modifiers");
    }

    public String getModifierPrefix() {
        return config.getString("modifier-prefix");
    }

    public String getModifierSuffix() {
        return config.getString("modifier-suffix");
    }
}
