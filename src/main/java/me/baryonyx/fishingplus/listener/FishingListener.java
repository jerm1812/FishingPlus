package me.baryonyx.fishingplus.listener;

import me.baryonyx.fishingplus.configuration.Config;
import me.baryonyx.fishingplus.fishing.CatchEvent;
import me.baryonyx.fishingplus.fishing.Competition.Competition;
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
    private Runner runner;
    private CatchEvent catchEvent;


    public FishingListener(Config config, Runner runner, CatchEvent catchEvent) {
        this.config = config;
        this.runner = runner;
        this.catchEvent = catchEvent;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void playerFishEvent(@NotNull PlayerFishEvent event) {
        // If using a FishingPlus reward is appropriate
        if (Competition.isRunning() || !config.rewardsOnlyDuringCompetition()) {
            // If a player caught a fish
            if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH && event.getCaught() instanceof Item) {
                Item caught = (Item) event.getCaught();
                ItemStack fish = catchEvent.handleCatchEvent(event.getPlayer());

                if (fish != null) {
                    caught.setItemStack(fish);
                    runner.logFish(event.getPlayer(), fish);
                }
            }
        }
    }
}
