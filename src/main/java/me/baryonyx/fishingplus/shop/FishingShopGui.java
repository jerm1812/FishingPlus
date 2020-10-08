package me.baryonyx.fishingplus.shop;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

public class FishingShopGui {
    private Inventory inventory;

    public FishingShopGui() {
        inventory = Bukkit.createInventory(null, 9);
        setupInventory();
    }

    private void setupInventory() {
        inventory.addItem(createInventoryItem(Material.LIGHT_BLUE_STAINED_GLASS_PANE, "", ""));
        inventory.addItem(createInventoryItem(Material.LIGHT_BLUE_STAINED_GLASS_PANE, "", ""));
        inventory.addItem(createInventoryItem(Material.LIGHT_BLUE_STAINED_GLASS_PANE, "", ""));
        inventory.addItem(createInventoryItem(Material.LIGHT_BLUE_STAINED_GLASS_PANE, "", ""));
        inventory.addItem(createInventoryItem(Material.EMERALD, "Sell All Items", "Total: 0"));
        inventory.addItem(createInventoryItem(Material.LIGHT_BLUE_STAINED_GLASS_PANE, "", ""));
        inventory.addItem(createInventoryItem(Material.LIGHT_BLUE_STAINED_GLASS_PANE, "", ""));
        inventory.addItem(createInventoryItem(Material.LIGHT_BLUE_STAINED_GLASS_PANE, "", ""));
        inventory.addItem(createInventoryItem(Material.LIGHT_BLUE_STAINED_GLASS_PANE, "", ""));
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

    public void dropItems(Inventory inventory, Player player) {
        ItemStack[] items = inventory.getContents();
        Map<Integer, ItemStack> map = player.getInventory().addItem(items);

        for (ItemStack item : map.values()) {
            player.getWorld().dropItemNaturally(player.getLocation(), item);
        }
    }

    public void updateTotal(Inventory inventory, double total) {
        ItemStack item = inventory.getItem(4);
        ItemMeta meta = item.getItemMeta();
        meta.setLore(Collections.singletonList("Total: " + total));
        item.setItemMeta(meta);
    }

    //TODO Make a GUI for the shop
}
