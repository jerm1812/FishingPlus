package me.baryonyx.fishingplus.handlers;

import me.baryonyx.fishingplus.FishingPlus;
import me.baryonyx.fishingplus.configuration.Config;
import me.baryonyx.fishingplus.messaging.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;

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

    // Creates an item to be mapped
    @NotNull
    private ItemStack createMappableItemFromReward(String displayName, Material material, int amount, List<String> lore, int modelData, List<PotionEffect> effects, int potionColor) {
        ItemStack item = new ItemStack(material, amount);
        if (material != Material.POTION || effects.isEmpty()) {
            setItemMeta(displayName, lore, item, modelData);
        }
        else {
            setItemMeta(displayName, lore, item, modelData, effects, potionColor);
        }

        return item;
    }

    // Sets the items display name and lore
    private void setItemMeta(String displayName, List<String> lore, @NotNull ItemStack item, int modelData) {
        try {
            ItemMeta meta = Objects.requireNonNull(item.getItemMeta());
            meta.setDisplayName(displayName);
            meta.setLore(convertLoreListToColor(lore));
            if (modelData != 0) {
                meta.setCustomModelData(modelData);
            }
            item.setItemMeta(meta);
        } catch (NullPointerException e) {
            Bukkit.getLogger().warning("Item's meta was null while it was being set");
        }
    }

    // Sets the items display name, lore, and adds potion effects
    private void setItemMeta(String displayName, List<String> lore, @NotNull ItemStack item, int modelData, List<PotionEffect> effects, int potionColor) {
        try {
            PotionMeta meta = (PotionMeta) Objects.requireNonNull(item.getItemMeta());
            meta.setDisplayName(displayName);
            meta.setLore(convertLoreListToColor(lore));
            for (PotionEffect effect : effects) {
                meta.addCustomEffect(effect, true);
            }
            if (modelData != 0) {
                meta.setCustomModelData(modelData);
            }

            if (potionColor != 0) {
                meta.setColor(Color.fromBGR(potionColor));
            }
            item.setItemMeta(meta);
        } catch (NullPointerException e) {
            Bukkit.getLogger().warning("Item's meta was null while it was being set");
        }
    }

    // Adds an item to the item map
    public void addItemToMap(String itemName, String displayName, Material material, int amount, List<String> lore, int modelData, List<PotionEffect> effects, int potionColor) {
        try {
            if (material == null) {
                return;
            }

            ItemStack item = createMappableItemFromReward(displayName, material, amount, lore, modelData, effects, potionColor);
            itemMap.put(itemName, item);
        } catch (DuplicateKeyException e) {
            Bukkit.getLogger().warning("There are duplicate reward names! Please remove one or change the name of one.");
        }
    }

    // Creates a FishingPlus reward item with persistent data
    @Nullable
    public ItemStack createRewardItem(String itemName, String playerName, boolean catchEvent) {
        ItemStack item = getItemFromMap(itemName);

        if (item == null) return null;

        if (config.getConfigBool("display-who-caught") && catchEvent) {
            addLoreToItemReward(item, "&7" + playerName + " caught this!");
        }

        addRewardPersistentData(item, itemName);

        return item;
    }

    // Creates a FishingPlus fish item with persistent data
    @Nullable
    public ItemStack createFishItem(String name, String playerName, double length) {
        ItemStack item = createRewardItem(name, playerName, true);

        if (item == null) return null;

        addLoreToItemReward(item, "&6Fish length: " + length);
        addFishLengthPersistentData(item, length);

        return item;
    }

    // Adds a fish length to the item's persistent data
    private void addFishLengthPersistentData(@NotNull ItemStack item, double length) {
        try {
            ItemMeta meta = Objects.requireNonNull(item.getItemMeta());
            meta.getPersistentDataContainer().set(lengthKey, PersistentDataType.DOUBLE, length);
            item.setItemMeta(meta);
        } catch (NullPointerException e) {
            Bukkit.getLogger().warning("Could not add fish's length persistent data because item's meta was null");
        }
    }

    // Adds the FishingPlus reward name to the item's persistent data
    private void addRewardPersistentData(@NotNull ItemStack item, String reward) {
        try {
            ItemMeta meta = Objects.requireNonNull(item.getItemMeta());
            meta.getPersistentDataContainer().set(rewardKey, PersistentDataType.STRING, reward);
            item.setItemMeta(meta);
        } catch (NullPointerException e) {
            Bukkit.getLogger().warning("Could not add reward's name persistent data because item's meta was null");
        }
    }

    // Returns a cloned item from the item map
    @Nullable
    private ItemStack getItemFromMap(String name) {
        ItemStack item = itemMap.get(name);
        return item == null ? null : item.clone();
    }

    // Adds a FishingPlus modifier to an item
    public void addModifierToItem(@NotNull ItemStack item, String modifierName) {
        try {
            ItemMeta meta = Objects.requireNonNull(item.getItemMeta());
            String name = Messages.coloredMessage(modifierName + meta.getDisplayName());
            meta.setDisplayName(name);
            meta.getPersistentDataContainer().set(modifierKey, PersistentDataType.STRING, modifierName);
            item.setItemMeta(meta);
        } catch (NullPointerException e) {
            Bukkit.getLogger().warning("Could not add modifier to a reward because the item's meta was null");
        }
    }

    // Adds a lore to an item
    private void addLoreToItemReward(@NotNull ItemStack item, String string) {
        try {
            ItemMeta meta = Objects.requireNonNull(item.getItemMeta());

            // If the item has lore add to it else just set it
            if (meta.hasLore()) {
                List<String> lore = Objects.requireNonNull(meta.getLore());
                lore.add(Messages.coloredMessage(string));
                meta.setLore(lore);
            }
            else
                meta.setLore(Collections.singletonList(Messages.coloredMessage(string)));

            item.setItemMeta(meta);
        } catch (NullPointerException e) {
            Bukkit.getLogger().warning("Could not add lore to a reward because the item's meta was null");
        }
    }

    // Converts a list of strings to minecraft color strings
    @NotNull
    public List<String> convertLoreListToColor(@NotNull List<String> lore) {
        return lore.stream().map(Messages::coloredMessage).collect(Collectors.toList());
    }

    // Returns if the item is a FishingPlus reward
    public boolean isReward(@NotNull ItemStack item) {
        try {
            return Objects.requireNonNull(Objects.requireNonNull(item.getItemMeta()).getPersistentDataContainer()).has(rewardKey, PersistentDataType.STRING);

        } catch (NullPointerException e) {
            Bukkit.getLogger().warning("Tried to see if an item was a FishingPlus reward but the item's meta was null");
        }

        return false;
    }

    // Returns the item's FishingPlus reward name
    @Nullable
    public String getRewardName(@NotNull ItemStack item) {
        try {
            return Objects.requireNonNull(item.getItemMeta()).getPersistentDataContainer().get(rewardKey, PersistentDataType.STRING);
        } catch (NullPointerException e) {
            Bukkit.getLogger().warning("Tried to get a FishingPlus reward name from an item but the item meta was null");
        }

        return null;
    }

    // Returns if the item is a FishingPlus fish from the persistent item data
    public boolean isFish(@NotNull ItemStack item) {
        try {
            return Objects.requireNonNull(item.getItemMeta()).getPersistentDataContainer().has(lengthKey, PersistentDataType.DOUBLE);
        } catch (NullPointerException e) {
            Bukkit.getLogger().warning("Tried to see if an item was a FishingPlus fish but the item's meta was null");
        }

        return false;
    }

    // Returns the fish length from the persistent item data
    public double getFishLength(@NotNull ItemStack item) {
        try {
            ItemMeta meta = Objects.requireNonNull(item.getItemMeta());
            PersistentDataContainer data = Objects.requireNonNull(meta.getPersistentDataContainer());
            return Objects.requireNonNull(data.get(lengthKey, PersistentDataType.DOUBLE));
        } catch (NullPointerException e) {
            Bukkit.getLogger().warning("Tried to get a FishingPlus fish length from an item but item meta was null");
        }

        return 0;
    }

    // Returns if the item has a FishingPlus modifier
    public boolean hasModifier(@NotNull ItemStack item) {
        try {
            return Objects.requireNonNull(item.getItemMeta()).getPersistentDataContainer().has(modifierKey, PersistentDataType.STRING);
        } catch (NullPointerException e) {
            Bukkit.getLogger().warning("Tried to see if an item had a FishingPlus modifier but item meta was null");
        }

        return false;
    }

    // Returns the item's FishingPlus modifier
    @Nullable
    public String getModifierName(@NotNull ItemStack item) {
        try {
            return Objects.requireNonNull(item.getItemMeta()).getPersistentDataContainer().get(modifierKey, PersistentDataType.STRING);
        } catch (NullPointerException e) {
            Bukkit.getLogger().warning("Tried to get a FishingPlus modifier from an item but item meta was null");
        }

        return null;
    }

    public void clear() {
        itemMap.clear();
    }
}
