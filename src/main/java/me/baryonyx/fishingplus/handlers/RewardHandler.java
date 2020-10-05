package me.baryonyx.fishingplus.handlers;

import me.baryonyx.fishingplus.FishingPlus;
import me.baryonyx.fishingplus.configuration.Config;
import me.baryonyx.fishingplus.fishing.Modifier;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import me.baryonyx.fishingplus.exceptions.InvalidFishLengthException;
import me.baryonyx.fishingplus.exceptions.ItemNotFoundException;
import me.baryonyx.fishingplus.exceptions.NoChanceToCatchException;
import me.baryonyx.fishingplus.fishing.Fish;
import me.baryonyx.fishingplus.fishing.Reward;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RewardHandler {
    private Config config;
    private NamespacedKey fishLength;
    private NamespacedKey rewardModifier;
    private NamespacedKey rewardType;

    public RewardHandler(Config config) {
        this.config = config;
        this.fishLength = new NamespacedKey(FishingPlus.getPlugin(), "fishLength");
        this.rewardModifier = new NamespacedKey(FishingPlus.getPlugin(), "rewardModifier");
        this.rewardType = new NamespacedKey(FishingPlus.getPlugin(), "rewardType");
    }

    private String getRewardDisplayName(@NotNull ConfigurationSection section, @NotNull String name) {
        return config.getRewardPrefix() + section.getString(name + ".display-name") + config.getRewardSuffix();
    }

    private double getMinFishLength(@NotNull ConfigurationSection section, @NotNull String name) throws InvalidFishLengthException {
        double minLength = section.getDouble(name + ".min-length");

        if (minLength < 0)
            throw new InvalidFishLengthException(name, minLength);

        return minLength;
    }

    private double getMaxFishLength(@NotNull ConfigurationSection section, @NotNull String name) throws InvalidFishLengthException {
        double maxLength = section.getDouble(name + ".max-length");

        if (maxLength < 0)
            throw new InvalidFishLengthException(name, maxLength);

        return maxLength;
    }

    private double getRewardChance(@NotNull ConfigurationSection section, @NotNull String name) throws NoChanceToCatchException {
        double chance = section.getDouble(name + ".chance");

        if (chance == 0)
            throw new NoChanceToCatchException(name);

        return chance;
    }

    @NotNull
    private Material getRewardMaterial(@NotNull ConfigurationSection section, @NotNull String name) throws ItemNotFoundException {
        String itemName = section.getString(name + ".item.id");

        if (itemName == null)
            throw new ItemNotFoundException(null, name);

        Material material = Material.matchMaterial(itemName);

        if (material == null)
            throw new ItemNotFoundException(itemName, name);

        return material;
    }

    @NotNull
    private List<String> getRewardLore(@NotNull ConfigurationSection section, @NotNull String name) {
        return section.getStringList(name + ".item.lore");
    }

    private double getRewardPrice(@NotNull ConfigurationSection section, @NotNull String name) {
        return section.getDouble(name + ".price", 0);
    }

    private int getRewardAmount(@NotNull ConfigurationSection section, @NotNull String name) {
        return section.getInt(name + ".item.amount", 1);
    }

    @Nullable
    public Reward loadNewReward(@NotNull ConfigurationSection section, @NotNull String name) {
        try {
            String displayName = getRewardDisplayName(section, name);
            Material material = getRewardMaterial(section, name);
            double chance = getRewardChance(section, name);
            double price = getRewardPrice(section, name);
            double maxLength = getMaxFishLength(section, name);
            double minLength = getMinFishLength(section, name);
            int amount = getRewardAmount(section, name);
            List<String> lore = getRewardLore(section, name);

            return (maxLength == 0) ?
                    new Reward(name, displayName, material, chance, price, lore, amount) :
                    new Fish(name, displayName, material, chance, price, minLength, maxLength, lore, amount);
        }
        catch (ItemNotFoundException e) {
            Bukkit.getLogger().warning("Could not load the reward " + e.getRewardName()
                    + " because " + e.getItemName() + " is an invalid material");
        }
        catch (NoChanceToCatchException e) {
            Bukkit.getLogger().warning("There is no chance to catch " + e.getRewardName()
                    + ". Leaving it out of the reward rewardMap");
        }
        catch (InvalidFishLengthException e) {
            Bukkit.getLogger().warning("There was a invalid fish length of " + e.getLength()
                    + " on this fish " + e.getItemName());
        }

        return null;
    }

    @Nullable
    public ItemStack createRewardItem(@NotNull Player player, @NotNull Reward reward, Modifier modifier) {
        try {
            ItemStack item = new ItemStack(reward.item, reward.amount);

            ItemMeta meta = createRewardMeta(player, reward, item, modifier);
            item.setItemMeta(meta);

            return item;
        }
        catch (ItemNotFoundException e) {
            Bukkit.getLogger().severe("There was an error creating the reward for " + player.getName()
                    + "");
        }

        return null;
    }

    private ItemMeta createRewardMeta(@NotNull Player player, Reward reward, @NotNull ItemStack item, @Nullable Modifier modifier) throws ItemNotFoundException {
        ItemMeta meta = item.getItemMeta();

        if (meta == null)
            throw new ItemNotFoundException(reward.item.name(), reward.name);

        PersistentDataContainer persistentData = meta.getPersistentDataContainer();
        String name;

        if (modifier != null || !modifier.displayName.equals("")) {
            name = modifier.displayName + reward.displayName;
            persistentData.set(rewardModifier, PersistentDataType.STRING, modifier.name);
        }

        else
            name = reward.displayName;

        if (reward instanceof Fish)
            persistentData.set(fishLength, PersistentDataType.DOUBLE, ((Fish) reward).actualLength);

        meta.setDisplayName(convertToColor(name));
        persistentData.set(rewardType, PersistentDataType.STRING, reward.name);
        List<String> lore = createRewardLore(player, reward);
        meta.setLore(convertLoreListToColor(lore));

        return meta;
    }

    private List<String> createRewardLore(@NotNull Player player, @NotNull Reward reward) {
        List<String> lore = new ArrayList<>(reward.lore);

        if (reward instanceof Fish) {
            lore.add("&7" + generateWeightedLength((Fish) reward) + " inches");
        }

        if (config.getDisplayWhoCaught())
            lore.add("&7" + player.getName() + " caught this!");

        return lore;
    }

    @NotNull
    private String convertToColor(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    @NotNull
    private List<String> convertLoreListToColor(@NotNull List<String> lore) {
        return lore.stream().map(this::convertToColor).collect(Collectors.toList());
    }

    private double generateWeightedLength(@NotNull Fish fish) {
        return Math.round((fish.minLength + (fish.maxLength + 1 - fish.minLength) *
                Math.pow(Math.random(), config.getFishLengthWeight())) * 100d) / 100d;
    }

    @NotNull
    private String getModifierDisplayName(@NotNull ConfigurationSection section, @NotNull String name) {
        String displayName = section.getString(name + ".display-name");
        if (!displayName.equals("")) {
            return config.getModifierPrefix() + section.getString(name + ".display-name") + config.getModifierSuffix();
        }
        else
            return "";
    }

    private double getModifierPriceIncrease(@NotNull ConfigurationSection section, @NotNull String name) {
        return section.getDouble(name + ".price-increase");
    }

    private double getModifierChance(@NotNull ConfigurationSection section, @NotNull String name) {
        return section.getDouble(name + ".chance");
    }

    public Modifier loadNewModifier(ConfigurationSection section, String name) {
        String displayName = getModifierDisplayName(section, name);
        double priceIncrease = getModifierPriceIncrease(section, name);
        double chance = getModifierChance(section, name);

        return new Modifier(name, displayName, chance, priceIncrease);
    }
}
