package me.baryonyx.fishingplus;

import me.baryonyx.fishingplus.commands.MainCommand;
import me.baryonyx.fishingplus.configuration.Config;
import me.baryonyx.fishingplus.configuration.RewardConfiguration;
import me.baryonyx.fishingplus.fishing.Competition.Announcements;
import me.baryonyx.fishingplus.fishing.Competition.Competition;
import me.baryonyx.fishingplus.fishing.Competition.Runner;
import me.baryonyx.fishingplus.hooks.CitizensHook;
import me.baryonyx.fishingplus.listener.FishingListener;
import me.baryonyx.fishingplus.handlers.*;
import me.baryonyx.fishingplus.hooks.VaultHook;
import me.baryonyx.fishingplus.listener.ShopListener;
import me.baryonyx.fishingplus.shop.CitizensShop;
import me.baryonyx.fishingplus.shop.FishingShop;
import me.baryonyx.fishingplus.shop.FishingShopGui;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class FishingPlus extends JavaPlugin {
    private final Config config = new Config(this);
    private RewardConfiguration rewardConfiguration = new RewardConfiguration(this);
    private Competition competition = new Competition();
    private ModifierHandler modifierHandler = new ModifierHandler();
    private ItemHandler itemHandler;
    private RewardHandler rewardHandler;
    private CatchHandler catchHandler;
    private Runner runner;
    private FishingShop fishingShop;
    private FishingShopGui fishingShopGui;

    @Override
    public void onEnable() {
        // Plugin startup logic
        checkFiles();

        Announcements announcements = new Announcements(config);
        itemHandler = new ItemHandler(config, this);
        rewardHandler = new RewardHandler(config);
        catchHandler = new CatchHandler(this, rewardConfiguration, config, rewardHandler, itemHandler, modifierHandler);
        runner = new Runner(this, config, competition, itemHandler, rewardHandler, announcements);


        setupHooks();
        setupShop();
        registerEvents();
        registerCommands();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if (fishingShopGui.inventories.size() > 0)
            for (Player player : fishingShopGui.inventories.keySet()) {
                fishingShopGui.closeInventory(fishingShopGui.inventories.get(player), player);
            }

        runner.stopCompetition();
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
        getServer().getPluginManager().registerEvents(new FishingListener(config, catchHandler, runner), this);
        net.citizensnpcs.api.CitizensAPI.getTraitFactory().registerTrait(net.citizensnpcs.api.trait.TraitInfo.create(CitizensShop.class).withName("fishingshop"));
    }

    private void registerCommands() {
        getCommand("fishingplus").setExecutor(new MainCommand(this, catchHandler, fishingShop, fishingShopGui, runner));
    }

    // Sets the shop up and inventory listener if vault is hooked
    private void setupShop() {
        if (VaultHook.isHooked) {
            fishingShop = new FishingShop(itemHandler, rewardHandler);
            fishingShopGui = new FishingShopGui(fishingShop, itemHandler, this);
            getServer().getPluginManager().registerEvents(new ShopListener(fishingShopGui, fishingShop, itemHandler), this);
        }
    }

    // Registers hooks
    private void setupHooks() {
        VaultHook.hook(this);
        CitizensHook.hook(this);
    }

    //TODO add a biome check and fish based on their biome
}
