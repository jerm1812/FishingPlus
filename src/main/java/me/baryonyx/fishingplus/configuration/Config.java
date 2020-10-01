package me.baryonyx.fishingplus.configuration;

import me.baryonyx.fishingplus.FishingPlus;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;


public class Config {
    private File datafile;
    private FishingPlus plugin;
    private YamlConfiguration rewardConfig;

    public Config(FishingPlus plugin) {
        this.plugin = plugin;
        datafile = this.plugin.getDataFolder();
        loadRewards();
    }

    private void loadRewards() {
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
}
