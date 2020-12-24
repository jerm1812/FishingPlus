package me.baryonyx.fishingplus.commands;

import me.baryonyx.fishingplus.fishing.shop.FishingShop;
import me.baryonyx.fishingplus.fishing.shop.FishingShopGui;
import me.baryonyx.fishingplus.hooks.VaultHook;
import me.baryonyx.fishingplus.messaging.Messages;
import me.baryonyx.fishingplus.utils.Permissions;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public class ShopCommand {
    private Messages messages;
    private FishingShop fishingShop;
    private FishingShopGui fishingShopGui;

    public ShopCommand(Messages messages, FishingShop fishingShop, FishingShopGui fishingShopGui) {
        this.messages = messages;
        this.fishingShop = fishingShop;
        this.fishingShopGui = fishingShopGui;
    }

    // Issuing the shop command
    boolean run(@NotNull Player player) {
        if (!player.hasPermission(Permissions.shop)) {
            messages.noPermission(player);
            return true;
        }

        // If the server does not have vault installed
        if (!VaultHook.isHooked) {
            messages.shopDisabled(player);
            return true;
        }

        // Creating shop inventory
        Inventory inventory = fishingShopGui.setupInventory(player);
        fishingShopGui.inventories.put(player, inventory);
        player.openInventory(inventory);
        return true;
    }

    // Issuing the sell all command
    boolean sellAll(Player player) {
        if (!player.hasPermission(Permissions.sellAll)) {
            messages.noPermission(player);
        }
        else {
            fishingShop.sellRewards(player.getInventory(), player);
        }

        return true;
    }
}
