package me.baryonyx.fishingplus.configuration;

import me.baryonyx.fishingplus.FishingPlus;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

public class Config {
    private FishingPlus plugin;
    private FileConfiguration config;
    private YamlConfiguration messages;

    public Config(@NotNull FishingPlus plugin) {
        this.plugin = plugin;
        loadConfig();
        loadMessages();
    }

    private void loadConfig() {
        File file = new File(plugin.getDataFolder(), "config.yml");

        if (!file.exists()) {
            plugin.saveDefaultConfig();
        }

        config = YamlConfiguration.loadConfiguration(file);
    }

    private void loadMessages() {
        File file = new File(plugin.getDataFolder(), "messages.yml");

        if (!file.exists()) {
            plugin.saveResource("messages.yml", false);
        }

        messages = YamlConfiguration.loadConfiguration(file);
    }

    private void save() {
        File file = new File(plugin.getDataFolder(), "config.yml");
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save the config!");
        }
    }

    public void reload() {
        config = null;
        loadConfig();
    }

    public void reloadMessages() {
        messages = null;
        loadMessages();
    }

    public String getConfigString(String key) {
        return config.getString(key);
    }

    public String getMessageString(String key) {
        return messages.getString(key);
    }

    public double getConfigDouble(String key) {
        return config.getDouble(key);
    }

    public boolean getConfigBool(String key) {
        return config.getBoolean(key);
    }

    public int getConfigInt(String key) {
        return config.getInt(key);
    }

    public List<String> getCompetitionRunTimes() {
        return config.getStringList("competition-run-times");
    }
}
