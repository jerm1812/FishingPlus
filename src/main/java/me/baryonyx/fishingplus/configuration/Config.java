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
    public YamlConfiguration yaml;
    public YamlConfiguration fishConfig;

    public Config(FishingPlus plugin) {
        this.plugin = plugin;
        datafile = this.plugin.getDataFolder();
    }

    public void loadFishTypes() {
        File file = new File(datafile, "FishConfig.yml");
        if (!file.exists()) { createFishConfig(); }
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
    }

    private void createFishConfig() {
        yaml = new YamlConfiguration();
        yaml.createSection("fish-types");
        saveFishTypes(yaml);
    }

    public void addFishType(String name, float minLength, float maxLength, Material item, float price) {
        yaml.set("fish-types." + name + ".price", price);
        yaml.set("fish-types." + name + ".maxLength", maxLength);
        yaml.set("fish-types." + name + ".minLength", minLength);
        yaml.set("fish-types." + name + ".item", item);
    }

    private void saveFishTypes(YamlConfiguration config) {
        File file = new File(datafile, "FishConfig.yml");
        try {
            config.save(file);
        } catch (IOException e) {
            Bukkit.getLogger().severe("Could not save fish types");
            Bukkit.getLogger().severe(e.getMessage());
        }
    }

    public ConfigurationSection getFishTypes() {
        return yaml.getConfigurationSection("fish-Types");
    }

    public void addRewardRarity() {

    }
}
