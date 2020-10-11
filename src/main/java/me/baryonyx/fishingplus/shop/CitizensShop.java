package me.baryonyx.fishingplus.shop;

import me.baryonyx.fishingplus.hooks.VaultHook;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.trait.Trait;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.Inventory;

public class CitizensShop extends Trait {
    private FishingShopGui fishingShopGui;

    public CitizensShop(FishingShopGui fishingShopGui) {
        super("fishingshop");
        this.fishingShopGui = fishingShopGui;
    }

    @EventHandler
    public void onNpcClick(NPCRightClickEvent event) {
        if (VaultHook.isHooked) {
            Inventory inventory = fishingShopGui.setupInventory(event.getClicker());
            fishingShopGui.inventories.put(event.getClicker(), inventory);
            event.getClicker().openInventory(inventory);
        }
        else
            event.getClicker().sendMessage("The FishingShop is closed!");
    }
}
