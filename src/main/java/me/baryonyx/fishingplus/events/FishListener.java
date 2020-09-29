package me.baryonyx.fishingplus.events;

import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FishListener implements Listener {

    public FishListener() {

    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void playerFishEvent(PlayerFishEvent event) {
        if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH && event.getCaught() instanceof Item) {
            Item caught = (Item) event.getCaught();
            ItemStack fish = new ItemStack(Material.COAL);
            ItemMeta meta = fish.getItemMeta();
            meta.setDisplayName("TEST FISHY COAL");
            List<String> lores = Arrays.asList("test lore", "test lore 2");
            meta.setLore(lores);
            fish.setItemMeta(meta);
            caught.setItemStack(fish);
        }
    }
}
