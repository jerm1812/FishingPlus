package me.baryonyx.fishingplus;

import me.baryonyx.fishingplus.commands.MainCommand;
import me.baryonyx.fishingplus.configuration.Config;
import me.baryonyx.fishingplus.configuration.RewardConfiguration;
import me.baryonyx.fishingplus.listener.FishingListener;
import me.baryonyx.fishingplus.handlers.*;
import me.baryonyx.fishingplus.hooks.VaultHook;
import me.baryonyx.fishingplus.shop.FishingShop;
import me.baryonyx.fishingplus.shop.FishingShopGui;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class FishingPlus extends JavaPlugin {
    private Config config;
    private RewardConfiguration rewardConfiguration;
    private static FishingPlus plugin;
    private ItemHandler itemHandler;
    private RewardHandler rewardHandler;
    private ModifierHandler modifierHandler;
    private CatchHandler catchHandler;
    private CompetitionHandler competitionHandler;
    private FishingShop fishingShop;
    private FishingShopGui fishingShopGui;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        checkFiles();
        setupHooks();

        config = new Config(this);
        rewardConfiguration = new RewardConfiguration(plugin, plugin.getDataFolder());
        itemHandler = new ItemHandler(config, plugin);
        rewardHandler = new RewardHandler(config, rewardConfiguration);
        modifierHandler = new ModifierHandler(rewardConfiguration);
        catchHandler = new CatchHandler(plugin, rewardConfiguration, config, rewardHandler, itemHandler, modifierHandler);
        competitionHandler = new CompetitionHandler();
        fishingShop = new FishingShop(itemHandler, rewardHandler, config);
        fishingShopGui = new FishingShopGui();

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
        getServer().getPluginManager().registerEvents(new FishingListener(config, catchHandler, competitionHandler), this);
    }

    private void registerCommands() {
        getCommand("fishingplus").setExecutor(new MainCommand(plugin, catchHandler, fishingShop, fishingShopGui));
    }

    public static FishingPlus getPlugin() {
        return plugin;
    }

    private void setupHooks() {
        VaultHook.hook(this);
    }

    //TODO create a fully functioning reward config
    //TODO add a biome check
    //TODO add fish based on their biomes
}
