package me.baryonyx.fishingplus;

import me.baryonyx.fishingplus.commands.MainCommand;
import me.baryonyx.fishingplus.configuration.Config;
import me.baryonyx.fishingplus.events.FishListener;
import me.baryonyx.fishingplus.fishing.FishingMap;
import me.baryonyx.fishingplus.handlers.RewardHandler;
import me.baryonyx.fishingplus.hooks.VaultHook;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class FishingPlus extends JavaPlugin {
    private Config config;
    private static FishingPlus plugin;
    private FishingMap fishingMap;
    private RewardHandler rewardHandler;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        checkFiles();
        setupHooks();

        config = new Config(this);
        rewardHandler = new RewardHandler(config);
        fishingMap = new FishingMap(config, rewardHandler);

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
        getServer().getPluginManager().registerEvents(new FishListener(fishingMap), this);
    }

    private void registerCommands() {
        getCommand("fishingplus").setExecutor(new MainCommand(plugin, fishingMap));
    }

    public static FishingPlus getPlugin() {
        return plugin;
    }

    private void setupHooks() {
        VaultHook.hook(this);
    }

    //TODO create shop GUI
    //TODO create item reader to get Reward and Modifier from ItemStack
    //TODO create a hook into Vault and other economy plugins
    //TODO create a competition handler
    //TODO setup using commands
    //TODO setup permissions
    //TODO create a fully functioning reward config
    //TODO add a biome check
    //TODO add fish based on their biomes
    //TODO create a NPC shop
}
