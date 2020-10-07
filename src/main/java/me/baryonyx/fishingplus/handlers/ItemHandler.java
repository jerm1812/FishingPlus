package me.baryonyx.fishingplus.handlers;

import me.baryonyx.fishingplus.FishingPlus;
import me.baryonyx.fishingplus.configuration.Config;
import me.baryonyx.fishingplus.exceptions.ItemNotFoundException;
import me.baryonyx.fishingplus.fishing.Reward;
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
    public HashMap<String, ItemStack> itemMap = new HashMap<>();
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
    private ItemStack createMappableItemFromReward(String itemName, String displayName, Material material, int amount, List<String> lore) {
        ItemStack item = new ItemStack(material, amount);
        setItemMeta(displayName, lore, Objects.requireNonNull(item.getItemMeta(), "The item's meta must not be null!" + itemName));
        return item;
    }

    private void setItemMeta(String displayName, List<String> lore, @NotNull ItemMeta meta) {
        meta.setDisplayName(displayName);
        meta.setLore(lore);
    }

    public void addItemToMap(String itemName, String displayName, Material material, int amount, List<String> lore) {
        try {
            ItemStack item = createMappableItemFromReward(itemName, displayName, material, amount, lore);
            itemMap.put(itemName, item);
        }
        catch (Exception e) {
            Bukkit.getLogger().severe("Could not add an item to the item table");
        }
    }

    @Nullable
    public ItemStack createRewardItem(String itemName, String playerName) {
        ItemStack item = getItemFromMap(itemName);

        if (item == null) return null;

        ItemMeta meta = Objects.requireNonNull(item.getItemMeta());

        if (config.getDisplayWhoCaught())
            addLoreToItemReward(meta, "&7" + playerName + " caught this!");

        addRewardPersistentData(meta, itemName);

        return item;
    }

    @Nullable
    public ItemStack createFishItem(String name, String playerName, double length) {
        ItemStack item = createRewardItem(name, playerName);

        if (item == null) return null;

        addLoreToItemReward(Objects.requireNonNull(item.getItemMeta()), String.valueOf(length));
        addFishLengthPersistentData(item.getItemMeta(), length);

        return item;
    }

    private void addFishLengthPersistentData(@NotNull ItemMeta meta, double length) {
        meta.getPersistentDataContainer().set(lengthKey, PersistentDataType.DOUBLE, length);
    }

    private void addRewardPersistentData(@NotNull ItemMeta meta, String reward) {
        meta.getPersistentDataContainer().set(rewardKey, PersistentDataType.STRING, reward);
    }

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

    public void addModidiferToItem(@NotNull ItemMeta meta, String modifierName) {
        String name = meta.getDisplayName();
        meta.setDisplayName(modifierName + name);
        meta.getPersistentDataContainer().set(modifierKey, PersistentDataType.STRING, modifierName);
    }

    private void addLoreToItemReward(@NotNull ItemMeta meta, String string) {
        if (meta.hasLore())
            Objects.requireNonNull(meta.getLore(), "Lore must not be null!").add(string);
        else
            meta.setLore(Collections.singletonList(string));
    }

    @NotNull
    private String convertToColor(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    @NotNull
    private List<String> convertLoreListToColor(@NotNull List<String> lore) {
        return lore.stream().map(this::convertToColor).collect(Collectors.toList());
    }

    public boolean isReward(@NotNull ItemStack item) {
        return Objects.requireNonNull(item.getItemMeta()).getPersistentDataContainer().has(rewardKey, PersistentDataType.STRING);
    }

    public String getRewardName(@NotNull ItemStack item) {
        return Objects.requireNonNull(item.getItemMeta()).getPersistentDataContainer().get(rewardKey, PersistentDataType.STRING);
    }

    public boolean isFish(@NotNull ItemStack item) {
        return Objects.requireNonNull(item.getItemMeta()).getPersistentDataContainer().has(lengthKey, PersistentDataType.DOUBLE);
    }

    public double getFishLength(@NotNull ItemStack item) {
        return Objects.requireNonNull(item.getItemMeta()).getPersistentDataContainer().get(lengthKey, PersistentDataType.DOUBLE);
    }

    public boolean hasModifier(@NotNull ItemStack item) {
        return Objects.requireNonNull(item.getItemMeta()).getPersistentDataContainer().has(modifierKey, PersistentDataType.STRING);
    }

    public String getModifierName(@NotNull ItemStack item) {
        return Objects.requireNonNull(item.getItemMeta()).getPersistentDataContainer().get(modifierKey, PersistentDataType.STRING);
    }
}
