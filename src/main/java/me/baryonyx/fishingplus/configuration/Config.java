package me.baryonyx.fishingplus.configuration;

import me.baryonyx.fishingplus.FishingPlus;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Config {
    private FishingPlus plugin;
    private FileConfiguration config;
    private YamlConfiguration messages;
    private List<YamlConfiguration> rewardFiles = new ArrayList<>();
    private YamlConfiguration competitionRewards;

    public Config(@NotNull FishingPlus plugin) {
        this.plugin = plugin;
        loadConfig();
        loadMessages();
        loadFishingRewards();
        loadCompetitionRewards();
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

    private void loadFishingRewards() {
        File[] files = new File(plugin.getDataFolder(), "rewards").listFiles();
        List<String> enabledRewards = config.getStringList("enabled-reward-configs");

        if (files == null || files.length == 0) {
            plugin.saveResource("rewards/fish.yml", false);
            loadFishingRewards();
            return;
        }

        for (File file : files) {
            if (enabledRewards.contains(file.getName().substring(0, file.getName().lastIndexOf(".")))
                    && !file.getName().equalsIgnoreCase("competition-rewards.yml")) {
                rewardFiles.add(YamlConfiguration.loadConfiguration(file));
            }
        }

        if (rewardFiles.size() == 0) {
            plugin.getLogger().warning("Did not load any reward files. Are there any enabled in the config?");
        }
    }

    private void loadCompetitionRewards() {
        File file = new File(plugin.getDataFolder(), "rewards" + File.separator + "competition-rewards.yml");

        if (!file.exists()) {
            plugin.saveResource("resources/competition-rewards.yml", false);
        }

        competitionRewards = YamlConfiguration.loadConfiguration(file);
    }

    public void reloadConfig() {
        config = null;
        loadConfig();
    }

    public void reloadMessages() {
        messages = null;
        loadMessages();
    }

    public void reloadRewards() {
        rewardFiles.clear();
        competitionRewards = null;
        loadFishingRewards();
        loadCompetitionRewards();
    }

    public String getConfigString(String key) {
        return config.getString(key);
    }

    public List<String> getConfigStringList(String key) {
        return config.getStringList(key);
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

    public List<ConfigurationSection> getRewardSections(String key) {
        List<ConfigurationSection> sections = new ArrayList<>();

        for(YamlConfiguration file : rewardFiles) {
            sections.add(file.getConfigurationSection(key));
        }

        return sections;
    }

    public ConfigurationSection getCompetitionRewardSection(String key) {
        return competitionRewards.getConfigurationSection(key);
    }
}
