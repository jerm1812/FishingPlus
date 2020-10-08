package me.baryonyx.fishingplus.listener;

import me.baryonyx.fishingplus.configuration.Config;
import me.baryonyx.fishingplus.handlers.CatchHandler;
import me.baryonyx.fishingplus.handlers.CompetitionHandler;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

public class FishingListener implements Listener {
    private final Config config;
    private final CatchHandler catchHandler;
    private CompetitionHandler competitionHandler;

    public FishingListener(Config config, CatchHandler catchHandler, CompetitionHandler competitionHandler) {
        this.config = config;
        this.catchHandler = catchHandler;
        this.competitionHandler = competitionHandler;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void playerFishEvent(PlayerFishEvent event) {
        if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH && event.getCaught() instanceof Item) {
            Item caught = (Item) event.getCaught();
            ItemStack fish = catchHandler.triggerCatchEvent(event.getPlayer());
            caught.setItemStack(fish);
        }
    }
}
