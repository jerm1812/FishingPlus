package me.baryonyx.fishingplus.hooks;

import me.baryonyx.fishingplus.FishingPlus;
import net.coreprotect.CoreProtect;
import net.coreprotect.CoreProtectAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class CoreProtectHook {
    private static CoreProtectAPI coreProtect;
    public static boolean isHooked = false;

    public static void hook(@NotNull FishingPlus plugin) {
        Plugin cp = plugin.getServer().getPluginManager().getPlugin("CoreProtect");

        if (!(cp instanceof CoreProtect)) {
            return;
        }

        coreProtect = ((CoreProtect) cp).getAPI();

        if (!coreProtect.isEnabled()) {
            Bukkit.getLogger().warning("Tried to hook CoreProtect but it is not enabled");
            return;
        }

        if (coreProtect.APIVersion() < 6) {
            Bukkit.getLogger().warning("Tried to hook CoreProtect but your version is too far outdated");
            return;
        }

        isHooked = true;
    }
}
