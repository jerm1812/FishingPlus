package me.baryonyx.fishingplus.events;

import me.baryonyx.fishingplus.fishing.RewardMap;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

public class FishListener implements Listener {
    private final RewardMap rewardMap;

    public FishListener(RewardMap rewardMap) {
        this.rewardMap = rewardMap;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void playerFishEvent(PlayerFishEvent event) {
        if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH && event.getCaught() instanceof Item) {
            Item caught = (Item) event.getCaught();
            ItemStack fish = rewardMap.createReward(event.getPlayer());
            caught.setItemStack(fish);
        }
    }
}
