package me.baryonyx.fishingplus.commands;

import me.baryonyx.fishingplus.fishing.CatchEvent;
import me.baryonyx.fishingplus.utils.Permissions;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TestCommand {
    private CatchEvent catchEvent;

    public TestCommand(CatchEvent catchEvent) {
        this.catchEvent = catchEvent;
    }

    boolean run(@NotNull Player player) {
        // Permission check
        if (player.hasPermission(Permissions.test)) {
            List<ItemStack> fish = new ArrayList<>();

            // Getting fish
            for (int i = 0; i < 36; i++) {
                fish.add(catchEvent.handleCatchEvent(player));
            }

            // Adding fish to players inventory
            Map<Integer, ItemStack> map = player.getInventory().addItem((ItemStack[])fish.toArray());

            // Dropping items that couldn't be placed in inventory
            for (ItemStack item : map.values()) {
                player.getWorld().dropItemNaturally(player.getLocation(), item);
            }
        }

        return true;
    }
}
