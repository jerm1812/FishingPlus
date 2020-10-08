package me.baryonyx.fishingplus.handlers;

import me.baryonyx.fishingplus.FishingPlus;
import me.baryonyx.fishingplus.configuration.Config;
import me.baryonyx.fishingplus.exceptions.ItemNotFoundException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class ItemHandler {
    private Config config;
    private HashMap<String, ItemStack> itemMap = new HashMap<>();
    private NamespacedKey lengthKey;
    private NamespacedKey modifierKey;
    private NamespacedKey rewardKey;

    public ItemHandler(Config config, FishingPlus plugin) {
        this.config = config;
        this.lengthKey = new NamespacedKey(plugin, "fishLength");
        this.modifierKey = new NamespacedKey(plugin, "rewardModifier");
        this.rewardKey = new NamespacedKey(plugin, "rewardType");
    }

    @NotNull
    private ItemStack createMappableItemFromReward(String displayName, Material material, int amount, List<String> lore) {
        ItemStack item = new ItemStack(material, amount);
        setItemMeta(displayName, lore, item);
        return item;
    }

    private void setItemMeta(String displayName, List<String> lore, @NotNull ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        Objects.requireNonNull(meta).setDisplayName(displayName);
        lore = convertLoreListToColor(lore);
        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    //
    void addItemToMap(String itemName, String displayName, Material material, int amount, List<String> lore) {
        try {
            ItemStack item = createMappableItemFromReward(displayName, material, amount, lore);
            itemMap.put(itemName, item);
        }
        catch (Exception e) {
            Bukkit.getLogger().severe("Could not add an item to the item table");
        }
    }

    // Creates and returns a FishingPlus reward with lore and attributes
    @Nullable
    ItemStack createRewardItem(String itemName, String playerName) {
        ItemStack item = getItemFromMap(itemName);

        if (item == null) return null;

        if (config.getDisplayWhoCaught())
            addLoreToItemReward(item, "&7" + playerName + " caught this!");

        addRewardPersistentData(item, itemName);

        return item;
    }

    // Creates and returns a FishingPlus fish with lore and attributes
    @Nullable
    ItemStack createFishItem(String name, String playerName, double length) {
        ItemStack item = createRewardItem(name, playerName);

        if (item == null) return null;

        addLoreToItemReward(item, "&6Fish length: " + length);
        addFishLengthPersistentData(item, length);

        return item;
    }

    // Adds a fish length to the item's persistent data
    private void addFishLengthPersistentData(@NotNull ItemStack item, double length) {
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(lengthKey, PersistentDataType.DOUBLE, length);
        item.setItemMeta(meta);
    }

    // Adds the FishingPlus reward name to the item's persistent data
    private void addRewardPersistentData(@NotNull ItemStack item, String reward) {
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(rewardKey, PersistentDataType.STRING, reward);
        item.setItemMeta(meta);
    }

    // Returns a cloned item from the item map
    @Nullable
    private ItemStack getItemFromMap(String name) {
        try {
            ItemStack item = itemMap.get(name);

            if (item == null)
                throw new ItemNotFoundException(name);

            return item.clone();
        } catch (ItemNotFoundException e) {
            Bukkit.getLogger().severe("Could not get the get the item with the reward name: " + e.getRewardName());
        }

        return null;
    }

    // Adds a FishingPlus modifier to an item
    void addModifierToItem(@NotNull ItemStack item, String modifierName) {
        ItemMeta meta = item.getItemMeta();
        String name = convertToColor(modifierName + meta.getDisplayName());
        meta.setDisplayName(name);
        meta.getPersistentDataContainer().set(modifierKey, PersistentDataType.STRING, modifierName);
        item.setItemMeta(meta);
    }

    // Adds a lore to an item
    private void addLoreToItemReward(@NotNull ItemStack item, String string) {
        ItemMeta meta = item.getItemMeta();

        // If the item has lore add to it else just set it
        if (meta.hasLore()) {
            List<String> lore = meta.getLore();
            lore.add(convertToColor(string));
            meta.setLore(lore);
        }
        else
            meta.setLore(Collections.singletonList(convertToColor(string)));

        item.setItemMeta(meta);
    }

    // Converts a string to use minecraft color
    @NotNull
    private String convertToColor(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    // Converts a list of strings to minecraft color strings
    @NotNull
    private List<String> convertLoreListToColor(@NotNull List<String> lore) {
        return lore.stream().map(this::convertToColor).collect(Collectors.toList());
    }

    // Returns if the item is a FishingPlus reward
    public boolean isReward(@NotNull ItemStack item) {
        return item.getItemMeta().getPersistentDataContainer().has(rewardKey, PersistentDataType.STRING);
    }

    // Returns the item's FishingPlus reward name
    public String getRewardName(@NotNull ItemStack item) {
        return Objects.requireNonNull(item.getItemMeta()).getPersistentDataContainer().get(rewardKey, PersistentDataType.STRING);
    }

    // Returns if the item is a FishingPlus fish from the persistent item data
    public boolean isFish(@NotNull ItemStack item) {
        return Objects.requireNonNull(item.getItemMeta()).getPersistentDataContainer().has(lengthKey, PersistentDataType.DOUBLE);
    }

    // Returns the fish length from the persistent item data
    public double getFishLength(@NotNull ItemStack item) {
        return Objects.requireNonNull(item.getItemMeta()).getPersistentDataContainer().get(lengthKey, PersistentDataType.DOUBLE);
    }

    // Returns if the item has a FishingPlus modifier
    public boolean hasModifier(@NotNull ItemStack item) {
        return Objects.requireNonNull(item.getItemMeta()).getPersistentDataContainer().has(modifierKey, PersistentDataType.STRING);
    }

    // Returns the item's FishingPlus modifier
    public String getModifierName(@NotNull ItemStack item) {
        return Objects.requireNonNull(item.getItemMeta()).getPersistentDataContainer().get(modifierKey, PersistentDataType.STRING);
    }

    //FIXME add more error handling
    // - make setting item meta, lore, and name more efficient
}
