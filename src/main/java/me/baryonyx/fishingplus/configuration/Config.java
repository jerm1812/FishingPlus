package me.baryonyx.fishingplus.configuration;

import com.google.inject.Inject;
import me.baryonyx.fishingplus.FishingPlus;
import me.baryonyx.fishingplus.competition.fish.Type;
import me.baryonyx.fishingplus.utils.SeedData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;

public class Config {
    private static Config config = new Config();
    public static Config getInstance() {
        return config;
    }
    private File datafile;
    @Inject private FishingPlus plugin;
    public YamlConfiguration yaml;

    private Config() {
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
        for (Type type : SeedData.seedFishTypes()) {
            addFishType(type.name, type.minLength, type.maxLength, type.item, type.price);
        }
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
}