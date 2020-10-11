package me.baryonyx.fishingplus.hooks;

import me.baryonyx.fishingplus.FishingPlus;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;

public class VaultHook {
    private static Economy economy;
    public static boolean isHooked = false;

    // Hooks vault
    public static boolean hook(@NotNull FishingPlus plugin) {
        RegisteredServiceProvider<Economy> service =  plugin.getServer().getServicesManager().getRegistration(Economy.class);

        if (service == null)
            return false;

        economy = service.getProvider();
        isHooked = true;
        return true;
    }

    // Returns the instance of the Vault economy
    public static Economy getEconomy() {
        return economy;
    }
}
