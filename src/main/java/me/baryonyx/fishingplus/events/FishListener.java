package me.baryonyx.fishingplus.events;

import me.baryonyx.fishingplus.fishing.FishingMap;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

public class FishListener implements Listener {
    private final FishingMap fishingMap;

    public FishListener(FishingMap fishingMap) {
        this.fishingMap = fishingMap;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void playerFishEvent(PlayerFishEvent event) {
        if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH && event.getCaught() instanceof Item) {
            Item caught = (Item) event.getCaught();
            ItemStack fish = fishingMap.createReward(event.getPlayer());
            caught.setItemStack(fish);
        }
    }
}
