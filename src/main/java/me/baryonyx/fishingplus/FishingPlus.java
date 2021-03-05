package me.baryonyx.fishingplus;

import me.baryonyx.fishingplus.commands.*;
import me.baryonyx.fishingplus.configuration.Config;
import me.baryonyx.fishingplus.configuration.ConfigUpdater;
import me.baryonyx.fishingplus.fishing.CatchEvent;
import me.baryonyx.fishingplus.fishing.Competition.Competition;
import me.baryonyx.fishingplus.fishing.Competition.Runner;
import me.baryonyx.fishingplus.fishing.Competition.TimerBar;
import me.baryonyx.fishingplus.fishing.RewardLoader;
import me.baryonyx.fishingplus.listener.FishingListener;
import me.baryonyx.fishingplus.handlers.*;
import me.baryonyx.fishingplus.hooks.VaultHook;
import me.baryonyx.fishingplus.listener.ShopListener;
import me.baryonyx.fishingplus.fishing.shop.FishingShop;
import me.baryonyx.fishingplus.fishing.shop.FishingShopGui;
import me.baryonyx.fishingplus.messaging.Messages;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public final class FishingPlus extends JavaPlugin {
    private Config config;
    private ItemHandler itemHandler;
    private RewardHandler rewardHandler;
    private ModifierHandler modifierHandler;
    private RewardLoader rewardLoader;
    private Runner runner;
    private CatchEvent catchEvent;
    private Messages messages;
    private FishingShop fishingShop;
    private FishingShopGui fishingShopGui;
    private CompetitionCommand competitionCommand;
    private ReloadCommand reloadCommand;
    private ShopCommand shopCommand;
    private TestCommand testCommand;
    private HelpCommand helpCommand;

    @Override
    public void onEnable() {
        // Plugin startup logic
        configCheck();

        config = new Config(this);
        TimerBar timerBar = new TimerBar(this, config);
        Competition competition = new Competition();
        modifierHandler = new ModifierHandler();
        messages = new Messages(this, config);
        itemHandler = new ItemHandler(config, this);
        rewardHandler = new RewardHandler(config);
        rewardLoader = new RewardLoader(this, config, rewardHandler, modifierHandler, itemHandler);
        runner = new Runner(this, config, competition, itemHandler, rewardHandler, messages, timerBar);
        competitionCommand = new CompetitionCommand(runner, messages);
        catchEvent = new CatchEvent(rewardHandler, modifierHandler, itemHandler);
        reloadCommand = new ReloadCommand(config, messages, rewardLoader);
        testCommand = new TestCommand(catchEvent);
        helpCommand = new HelpCommand();

        setupHooks();
        setupShop();

        shopCommand = new ShopCommand(messages, fishingShop, fishingShopGui);

        registerEvents();
        registerCommands();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if (fishingShopGui != null && fishingShopGui.inventories.size() > 0) {
            for (Player player : fishingShopGui.inventories.keySet()) {
                fishingShopGui.closeInventory(fishingShopGui.inventories.get(player), player);
            }
        }

        if (Competition.isRunning()) {
            runner.cancelCompetition();
        }

        HandlerList.unregisterAll(this);
        itemHandler.clear();
        rewardHandler.clear();
        modifierHandler.clear();
    }

    // Registers the events for the plugin
    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new FishingListener(config, runner, catchEvent), this);
    }

    // Registers the base command for the plugin
    private void registerCommands() {
        getCommand("fishingplus").setExecutor(new BaseCommand(this, competitionCommand, reloadCommand, shopCommand, testCommand, helpCommand));
        getCommand("fishingplus").setTabCompleter(new CommandCompleter());
    }

    // Sets the shop up and inventory listener if vault is hooked
    private void setupShop() {
        if (VaultHook.isHooked) {
            fishingShop = new FishingShop(itemHandler, config, messages);
            fishingShopGui = new FishingShopGui(fishingShop, itemHandler, this, config);
            getServer().getPluginManager().registerEvents(new ShopListener(fishingShopGui, fishingShop, itemHandler), this);
        }
    }

    // Registers hooks
    private void setupHooks() {
        VaultHook.hook(this);
    }

    private void configCheck() {
        ConfigUpdater configUpdater = new ConfigUpdater(this);
        configUpdater.checkConfigVersion();
    }

    //TODO add a biome check and fish based on their biome
}
