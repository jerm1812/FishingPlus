package me.baryonyx.fishingplus.configuration;

import me.baryonyx.fishingplus.FishingPlus;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ConfigUpdater {
    private FishingPlus plugin;
    private Config config;
    private String latestVersion = "1.0.0";
    
    public ConfigUpdater(FishingPlus plugin, Config config) {
        this.plugin = plugin;
        this.config = config;
    }

    public void checkConfigVersion() {
        String currentVersion = config.getConfigString("config-version");
        
        if (!latestVersion.equals(currentVersion)) {
            updateConfig();
        }
    }

    private void updateConfig() {
        if (!backupConfig()) {
            plugin.getLogger().severe("Could not backup the config while updating it!");
            return;
        }

        FileConfiguration oldConfig = plugin.getConfig();
        plugin.saveResource("config.yml", true);
        File file = new File(plugin.getDataFolder(), "config.yml");
        FileConfiguration newConfig = YamlConfiguration.loadConfiguration(file);

        for (String key : newConfig.getKeys(false)) {
            if (oldConfig.isSet(key) && !key.equals("config-version")) {
                newConfig.set(key, oldConfig.get(key));
            }
        }

        newConfig.set("config-version", latestVersion);
        config.reloadConfig();
    }

    private String getBackupNumber() {
        File[] files = new File(plugin.getDataFolder(), "configbackups").listFiles();

        if (files == null || files.length == 0) {
            return "";
        }

        int lastBackup = 1;

        for (File file : files) {
            try {
                int number = Integer.getInteger(file.getName().substring(8, file.getName().lastIndexOf(".")));

                if (number > lastBackup) {
                    lastBackup = number;
                }
            } catch (Exception ignored) {
            }
        }

        return String.valueOf(lastBackup);
    }

    private boolean backupConfig() {
        File file = new File(plugin.getDataFolder(), "config.yml");
        File newFile = new File(plugin.getDataFolder(), "configbackups" + File.separator + "oldconfig" + getBackupNumber() + ".yml");

        return file.renameTo(newFile);
    }
}
