package me.baryonyx.fishingplus.shop;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class FishingShopGui {
    private Inventory inventory;

    public FishingShopGui() {
        inventory = Bukkit.createInventory(null, 9);
        setupInventory();
    }

    private void setupInventory() {
        inventory.addItem(new ItemStack(Material.EMERALD));
    }

    private ItemStack createInventoryItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);
        return item;
    }

    public void openInventory(Player player) {
        player.openInventory(inventory);
    }

    public void dropItems() {
        inventory.getContents();

    }

    //TODO Make a GUI for the shop
}
