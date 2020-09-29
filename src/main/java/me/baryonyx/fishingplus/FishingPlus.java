package me.baryonyx.fishingplus;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import me.baryonyx.fishingplus.binding.PluginBinder;
import me.baryonyx.fishingplus.commands.MainCommand;
import me.baryonyx.fishingplus.configuration.Config;
import me.baryonyx.fishingplus.events.FishListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

@Singleton
public final class FishingPlus extends JavaPlugin {
    private Config config;

    @Override
    public void onEnable() {
        // Plugin startup logic
        setupInjection();
        checkFiles();
        registerEvents();
        registerCommands();
        config = Config.getInstance();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void setupInjection() {
        PluginBinder binder = new PluginBinder(this);
        Injector injector = binder.createInjector();
        injector.injectMembers(this);
    }

    private void checkFiles() {
        File file = new File(this.getDataFolder(), "config.yml");
        if (!file.exists()) {
            saveDefaultConfig();
        }
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new FishListener(), this);
    }

    private void registerCommands() {
        getCommand("fishingplus").setExecutor(new MainCommand());
    }

    public void applyConfig() {

    }
}
