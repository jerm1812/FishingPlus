package me.baryonyx.fishingplus.hooks;

import me.baryonyx.fishingplus.FishingPlus;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultHook {
    private static Economy economy;

    public static boolean hook(FishingPlus plugin) {
        RegisteredServiceProvider<Economy> service =  plugin.getServer().getServicesManager().getRegistration(Economy.class);

        if (service == null)
            return false;

        economy = service.getProvider();
        return true;
    }

    public Economy getEconomy() {
        return economy;
    }
}
