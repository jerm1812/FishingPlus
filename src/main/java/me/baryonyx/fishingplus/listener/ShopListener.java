package me.baryonyx.fishingplus.listener;

import me.baryonyx.fishingplus.shop.FishingShopGui;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public class ShopListener implements Listener {
    private FishingShopGui fishingShopGui;

    public ShopListener(FishingShopGui fishingShopGui) {
        this.fishingShopGui = fishingShopGui;
    }

    @EventHandler
    public void InventoryListener(@NotNull InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();

        // If the FishingPlus inventory was clicked
        if (fishingShopGui.inventories.containsValue(inventory)) {
            if (event.getSlot() == 31) {
                fishingShopGui.sellRewards((Player)event.getWhoClicked(), inventory);
            }
            if (event.getSlot() >= 27) {
                event.setCancelled(true);
            }

            fishingShopGui.calculateGuiTotal(inventory);
            return;
        }

        //FIXME Only allow FishingPlus rewards to be added to the FishingPlus shop inventories

        // If the player's inventory was clicked while in a FishingPlus shop
        if (fishingShopGui.inventories.containsValue(event.getInventory())) {
            fishingShopGui.calculateGuiTotal(fishingShopGui.getPlayersShop((Player)event.getWhoClicked()));
        }
    }

    @EventHandler
    public void closeShop(@NotNull InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();
        if (fishingShopGui.inventories.containsValue(inventory)) {
            fishingShopGui.closeInventory(inventory, (Player)event.getPlayer());
        }
    }
}
