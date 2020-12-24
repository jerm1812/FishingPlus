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

    public String getString(String key) {
        return config.getString(key);
    }

    public String getMessageString(String key) {
        return messages.getString(key);
    }

    public double getFishLengthWeight() {
        return config.getDouble("fish-length-weight");
    }

    public boolean getDisplayWhoCaught() {
        return config.getBoolean("display-who-caught");
    }

    public String getRewardNames() {
        return config.getString("reward-names");
    }

    public boolean getAllowModifiers() {
        return config.getBoolean("enable-modifiers");
    }

    public String getModifierNames() {
        return config.getString("modifier-names");
    }

    public boolean rewardsOnlyDuringCompetition() {
        return config.getBoolean("competition-only");
    }

    public boolean isBroadcastStartEnabled() {
        return config.getBoolean("broadcast-competition-start");
    }

    public boolean isBroadcastEndEnabled() {
        return config.getBoolean("broadcast-competition-end");
    }

    public String getBroadcastPrefix() {
        return config.getString("message-prefix");
    }

    public int getAmountOfWinnersDisplayed() {
        return config.getInt("players-displayed");
    }

    public int getMinimumParticipants() {
        return config.getInt("minimum-fishers");
    }

    public int getCompetitionDuration() {
        return config.getInt("competition-duration");
    }

    public List<String> getCompetitionRunTimes() {
        return config.getStringList("competition-run-times");
    }

    public double getPriceMultiplier() {
        return config.getDouble("price-multiplier");
    }

    public String getShopSellMessage() {
        return config.getString("sell-message");
    }

    public String getShopName() {
        return config.getString("shop-name");
    }

    public String getTimebarTitle() {
        return config.getString("timebar-title");
    }


}
