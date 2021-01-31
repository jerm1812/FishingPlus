package me.baryonyx.fishingplus.fishing.shop;

import me.baryonyx.fishingplus.FishingPlus;
import me.baryonyx.fishingplus.configuration.Config;
import me.baryonyx.fishingplus.handlers.ItemHandler;
import me.baryonyx.fishingplus.messaging.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class FishingShopGui implements Listener {
    private FishingShop fishingShop;
    private ItemHandler itemHandler;
    private final FishingPlus plugin;
    private Config config;
    private final ItemStack rowIcon = createInventoryItem(Material.LIGHT_BLUE_STAINED_GLASS_PANE, " ", " ");
    private ItemStack sellIcon = createInventoryItem(Material.EMERALD, "Sell All FishingPlus Rewards", "Total: 0");
    public Map<Player, Inventory> inventories = new HashMap<>();

    public FishingShopGui(FishingShop fishingShop, ItemHandler itemHandler, FishingPlus plugin, Config config) {
        this.fishingShop = fishingShop;
        this.itemHandler = itemHandler;
        this.plugin = plugin;
        this.config = config;
    }

    // Creates a FishingPlus shop inventory
    @NotNull
    public Inventory setupInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(player, 36, Messages.coloredMessage(config.getConfigString("shop-name")));
        inventory.setItem(35, rowIcon);
        inventory.setItem(34, rowIcon);
        inventory.setItem(33, rowIcon);
        inventory.setItem(32, rowIcon);
        inventory.setItem(31, sellIcon);
        inventory.setItem(30, rowIcon);
        inventory.setItem(29, rowIcon);
        inventory.setItem(28, rowIcon);
        inventory.setItem(27, rowIcon);
        return inventory;
    }

    // Creates an item for the shop
    @Nullable
    private ItemStack createInventoryItem(Material material, String name, String... lore) {
        try {
            ItemStack item = new ItemStack(material);
            ItemMeta meta = Objects.requireNonNull(item.getItemMeta());
            meta.setDisplayName(name);
            meta.setLore(Arrays.asList(lore));
            item.setItemMeta(meta);
            return item;
        } catch (NullPointerException e) {
            Bukkit.getLogger().warning("Could not create FishingPlus shop item because item meta was null");
        }

        return null;
    }

    // Returns a player's FishingPlus shop inventory
    public Inventory getPlayersShop(Player player) {
        return inventories.get(player);
    }

    // Cleans up a FishingPlus shop inventory when closed
    public void closeInventory(Inventory inventory, Player player) {
        dropItems(inventory, player);
        inventories.remove(player);
    }

    // Drops all non-shop items in a FishingPlus inventory
    private void dropItems(@NotNull Inventory inventory, @NotNull Player player) {
        ItemStack[] items = Arrays.stream(inventory.getContents()).filter(i -> i != null && itemHandler.isReward(i)).toArray(ItemStack[]::new);
        Map<Integer, ItemStack> map = player.getInventory().addItem(items);

        for (ItemStack item : map.values()) {
            player.getWorld().dropItemNaturally(player.getLocation(), item);
        }
    }

    // Sells all FishingPlus rewards in the FishingPlus shop inventory
    public void sellRewards(@NotNull Player player, Inventory inventory) {
        fishingShop.sellRewards(inventory, player);
    }

    // Calculates the total price for fish rewards at a 1 tick delay
    public void calculateGuiTotal(Inventory inventory) {
        // Uses one tick delay so it will calculate the price after an item is moved instead of during
        new BukkitRunnable() {
            @Override
            public void run() {
                Map<ItemStack, Double> map = fishingShop.getRewardsInInventory(inventory);
                double total = fishingShop.calculateTotalValue(map);
                updateTotal(inventory, total);
            }
        }.runTaskLater(plugin, 1);
    }

    // Updates the displayed total for a FishingPlus shop inventory
    private void updateTotal(@NotNull Inventory inventory, double total) {
        try {
            ItemStack item = Objects.requireNonNull(inventory.getItem(31));
            ItemMeta meta = Objects.requireNonNull(item.getItemMeta());
            meta.setLore(itemHandler.convertLoreListToColor(Collections.singletonList("&6Total: " + total)));
            item.setItemMeta(meta);
            inventory.setItem(31, item);
        } catch (NullPointerException e) {
            Bukkit.getLogger().warning("Tried to update the total in the shop but the item or item meta was null");
        }
    }
}
