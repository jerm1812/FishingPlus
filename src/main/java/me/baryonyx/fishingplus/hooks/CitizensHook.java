package me.baryonyx.fishingplus.hooks;

import me.baryonyx.fishingplus.FishingPlus;
import me.baryonyx.fishingplus.shop.CitizensShop;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.trait.TraitInfo;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public class CitizensHook {
    private static TraitInfo traitInfo;
    public static boolean isHooked = false;

    public static boolean hook(@NotNull FishingPlus plugin) {
        boolean hookable = plugin.getServer().getPluginManager().isPluginEnabled("Citizens");

        if (hookable) {
            traitInfo = TraitInfo.create(CitizensShop.class);
            CitizensAPI.getTraitFactory().registerTrait(traitInfo);
            isHooked = true;
            return true;
        }
        else {
            Bukkit.getLogger().warning("Vault is not enabled. Disabling FishingPlus shop.");
            return false;
        }
    }

    public void deregisterTrait() {
        CitizensAPI.getTraitFactory().deregisterTrait(traitInfo);
    }

}
