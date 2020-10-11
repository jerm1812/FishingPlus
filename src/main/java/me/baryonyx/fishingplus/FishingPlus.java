package me.baryonyx.fishingplus;

import me.baryonyx.fishingplus.commands.MainCommand;
import me.baryonyx.fishingplus.configuration.Config;
import me.baryonyx.fishingplus.configuration.RewardConfiguration;
import me.baryonyx.fishingplus.hooks.CitizensHook;
import me.baryonyx.fishingplus.listener.FishingListener;
import me.baryonyx.fishingplus.handlers.*;
import me.baryonyx.fishingplus.hooks.VaultHook;
import me.baryonyx.fishingplus.listener.ShopListener;
import me.baryonyx.fishingplus.shop.FishingShop;
import me.baryonyx.fishingplus.shop.FishingShopGui;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class FishingPlus extends JavaPlugin {
    private final Config config = new Config(this);
    private RewardConfiguration rewardConfiguration = new RewardConfiguration(this);
    private ItemHandler itemHandler;
    private RewardHandler rewardHandler;
    private ModifierHandler modifierHandler = new ModifierHandler();
    private CatchHandler catchHandler;
    private CompetitionHandler competitionHandler;
    private FishingShop fishingShop;
    private FishingShopGui fishingShopGui;

    @Override
    public void onEnable() {
        // Plugin startup logic
        checkFiles();
        setupHooks();

        itemHandler = new ItemHandler(config, this);
        rewardHandler = new RewardHandler(config);
        catchHandler = new CatchHandler(this, rewardConfiguration, config, rewardHandler, itemHandler, modifierHandler);
        competitionHandler = new CompetitionHandler(this);

        setupShop();

        registerEvents();
        registerCommands();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    // Saves default config and rewards config if they do not exist
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
        getCommand("fishingplus").setExecutor(new MainCommand(this, catchHandler, fishingShop, fishingShopGui));
    }

    // Sets the shop up and inventory listener if vault is hooked
    private void setupShop() {
        if (VaultHook.isHooked) {
            fishingShop = new FishingShop(itemHandler, rewardHandler);
            fishingShopGui = new FishingShopGui(fishingShop, itemHandler, this);
            getServer().getPluginManager().registerEvents(new ShopListener(fishingShopGui), this);
        }
    }

    // Registers hooks
    private void setupHooks() {
        VaultHook.hook(this);
        CitizensHook.hook(this);
    }

    //TODO create a fully functioning reward config
    //TODO add a biome check
    //TODO add fish based on their biomes
}
