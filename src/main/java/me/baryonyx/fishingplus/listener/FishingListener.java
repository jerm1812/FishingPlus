package me.baryonyx.fishingplus.listener;

import me.baryonyx.fishingplus.configuration.Config;
import me.baryonyx.fishingplus.fishing.Competition.Competition;
import me.baryonyx.fishingplus.handlers.CatchHandler;
import me.baryonyx.fishingplus.fishing.Competition.Runner;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class FishingListener implements Listener {
    private final Config config;
    private final CatchHandler catchHandler;
    private Runner runner;

    public FishingListener(Config config, CatchHandler catchHandler, Runner runner) {
        this.config = config;
        this.catchHandler = catchHandler;
        this.runner = runner;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void playerFishEvent(@NotNull PlayerFishEvent event) {
        // If using a FishingPlus reward is appropriate
        if (Competition.isRunning() || !config.rewardsOnlyDuringCompetition()) {
            // If a fish was caught
            if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH && event.getCaught() instanceof Item) {
                Item caught = (Item) event.getCaught();
                ItemStack fish = catchHandler.handleCatchEvent(event.getPlayer());
                if (fish != null) {
                    caught.setItemStack(fish);
                    runner.logFish(event.getPlayer(), fish);
                }
            }
        }
    }
}
