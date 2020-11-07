package me.baryonyx.fishingplus.listener;

import me.baryonyx.fishingplus.handlers.ItemHandler;
import me.baryonyx.fishingplus.fishing.shop.FishingShop;
import me.baryonyx.fishingplus.fishing.shop.FishingShopGui;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class ShopListener implements Listener {
    private FishingShopGui fishingShopGui;
    private FishingShop fishingShop;
    private ItemHandler itemHandler;

    public ShopListener(FishingShopGui fishingShopGui, FishingShop fishingShop, ItemHandler itemHandler) {
        this.fishingShopGui = fishingShopGui;
        this.fishingShop = fishingShop;
        this.itemHandler = itemHandler;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void InventoryListener(@NotNull InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();

        // If the FishingPlus inventory was clicked
        if (fishingShopGui.inventories.containsValue(inventory)) {
            if (event.getSlot() == 31) {
                fishingShop.sellRewards(inventory, (Player)event.getWhoClicked());
            }

            if (event.getSlot() >= 27) {
                event.setCancelled(true);
            }

            if (isIllegalItemMove(event.getAction(), event.getCurrentItem(), event.getCursor())){
                event.setCancelled(true);
            }

            fishingShopGui.calculateGuiTotal(inventory);
            return;
        }

        // If the player's inventory was clicked while in a FishingPlus shop
        if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY && fishingShopGui.inventories.containsValue(event.getInventory())) {

            if (isIllegalItemMove(event.getCurrentItem())) {
                event.setCancelled(true);
                return;
            }

            fishingShopGui.calculateGuiTotal(fishingShopGui.getPlayersShop((Player)event.getWhoClicked()));
        }
    }

    @EventHandler
    public void shopDragEvent(@NotNull InventoryDragEvent event) {
        if (fishingShopGui.inventories.containsValue(event.getView().getTopInventory())) {
            if (isIllegalItemMove(event.getCursor())) {
                event.setCancelled(true);
            }
        }
    }

    // Returns true if a non FishingPlus reward is being moved into the FishingPlus shop
    @Contract("_, null, _ -> true")
    private boolean isIllegalItemMove(InventoryAction action, ItemStack item, ItemStack cursor) {
        if (item == null) {
            return true;
        }
        else if ((action == InventoryAction.PLACE_ALL || action == InventoryAction.PLACE_ONE || action == InventoryAction.PLACE_SOME) && !itemHandler.isReward(item)) {
            return true;
        }
        else {
            return action == InventoryAction.SWAP_WITH_CURSOR && cursor != null && !itemHandler.isReward(cursor);
        }
    }

    @Contract("null -> true")
    private boolean isIllegalItemMove(ItemStack item) {
        return item == null || !itemHandler.isReward(item);
    }

    @EventHandler
    public void closeShop(@NotNull InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();
        if (fishingShopGui.inventories.containsValue(inventory)) {
            fishingShopGui.closeInventory(inventory, (Player)event.getPlayer());
        }
    }
}
