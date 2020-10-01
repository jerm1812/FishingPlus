package me.baryonyx.fishingplus;

import me.baryonyx.fishingplus.commands.MainCommand;
import me.baryonyx.fishingplus.configuration.Config;
import me.baryonyx.fishingplus.events.FishListener;
import me.baryonyx.fishingplus.fishing.RewardMap;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class FishingPlus extends JavaPlugin {
    private Config config;
    private static FishingPlus plugin;
    private RewardMap rewardMap;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        checkFiles();

        config = new Config(this);
        rewardMap = new RewardMap(config);

        registerEvents();
        registerCommands();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void checkFiles() {
        File file = new File(this.getDataFolder(), "config.yml");
        if (!file.exists())
            saveDefaultConfig();
        file = new File(this.getDataFolder(), "rewards.yml");
        if (!file.exists())
            saveResource("rewards.yml", false);
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new FishListener(rewardMap), this);
    }

    private void registerCommands() {
        getCommand("fishingplus").setExecutor(new MainCommand(plugin, rewardMap));
    }

    public void applyConfig() {

    }

    public static FishingPlus getPlugin() {
        return plugin;
    }
}
