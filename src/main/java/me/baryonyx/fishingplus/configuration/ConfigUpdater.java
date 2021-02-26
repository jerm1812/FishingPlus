package me.baryonyx.fishingplus.configuration;

import me.baryonyx.fishingplus.FishingPlus;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfigUpdater {
    private FishingPlus plugin;
    private int currentVersion;
    private final int latestVersion = 1;
    
    public ConfigUpdater(FishingPlus plugin) {
        this.plugin = plugin;
        File file = new File(plugin.getDataFolder(), "config.yml");

        if (!file.exists()) {
            plugin.saveDefaultConfig();
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        currentVersion = config.getInt("config-version", 0);
    }

    public void checkConfigVersion() {
        if (currentVersion < latestVersion) {
            updateConfig();
        }
    }

    private void updateConfig() {
        if (!backupConfig()) {
            plugin.getLogger().severe("Could not backup the config while updating it!");
            return;
        }

        File file = new File(plugin.getDataFolder(), "config.yml");
        List<String> list = getIgnoreList();

        try {
            com.tchristofferson.configupdater.ConfigUpdater.update(plugin, "config.yml", file, list);
        } catch (IOException e) {
            e.printStackTrace();
        }

        plugin.reloadConfig();
    }

    private String getBackupNumber() {
        File[] files = new File(plugin.getDataFolder(), "configbackups").listFiles();

        if (files == null || files.length == 0) {
            return "";
        }

        return String.valueOf(files.length);
    }

    private boolean backupConfig() {
        checkBackupDir();
        Path source = Paths.get(plugin.getDataFolder().getPath(), "config.yml");
        Path destination = Paths.get(plugin.getDataFolder().getPath(), "configbackups" + File.separator + "oldconfig" + getBackupNumber() + ".yml");

        try {
            Files.move(source, destination);
            return true;
        } catch (IOException e) {
            plugin.getLogger().severe(e.getMessage());
            return false;
        }
    }

    private void checkBackupDir() {
        Path folder = Paths.get(plugin.getDataFolder().getPath(), "configbackups");

        if (Files.notExists(folder)) {
            try {
                Files.createDirectories(folder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private List<String> getIgnoreList() {
        List<String> list = new ArrayList<>();

        if (currentVersion >= 1) {
            list.add("enabled-reward-configs");
        }

        if (currentVersion >= 0) {
            list.addAll(Arrays.asList("broadcast-competition-start", "broadcast-competition-end", "broadcast-competition-end",
                    "broadcast-competition-winner", "players-displayed", "minimum-fishers", "competition-run-times",
                    "competition-duration", "timebar-title", "competition-only", "display-who-caught", "reward-names",
                    "fish-length-weight", "display-fish-lengh", "enable-modifiers", "modifier-names", "shop-name",
                    "price-multiplier"));
        }

        return list;
    }
}
