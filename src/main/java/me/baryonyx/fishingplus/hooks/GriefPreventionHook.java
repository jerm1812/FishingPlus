package me.baryonyx.fishingplus.hooks;

import me.baryonyx.fishingplus.FishingPlus;
import me.ryanhamshire.griefprevention.GriefPrevention;
import me.ryanhamshire.griefprevention.api.GriefPreventionApi;
import org.bukkit.plugin.Plugin;

public class GriefPreventionHook {
    private static GriefPreventionApi griefPrevention;
    public static boolean isHooked = false;

    public static void hook(FishingPlus plugin) {
        Plugin gp = plugin.getServer().getPluginManager().getPlugin("GriefPrevention");

        if (gp == null) {
            return;
        }

        griefPrevention = GriefPrevention.getApi();
        isHooked = true;
    }

    public static GriefPreventionApi getGriefPrevention() {
        return griefPrevention;
    }
}
