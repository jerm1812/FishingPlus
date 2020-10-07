package me.baryonyx.fishingplus.handlers;

import me.baryonyx.fishingplus.FishingPlus;
import me.baryonyx.fishingplus.configuration.Config;
import me.baryonyx.fishingplus.configuration.RewardConfiguration;
import me.baryonyx.fishingplus.exceptions.InvalidFishLengthException;
import me.baryonyx.fishingplus.exceptions.ItemNotFoundException;
import me.baryonyx.fishingplus.exceptions.NoChanceToCatchException;
import me.baryonyx.fishingplus.fishing.Fish;
import me.baryonyx.fishingplus.fishing.Modifier;
import me.baryonyx.fishingplus.fishing.Reward;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MapHandler {
    private final FishingPlus plugin;
    private final RewardConfiguration rewardConfiguration;
    private final Config config;
    private RewardHandler rewardHandler;
    private ItemHandler itemHandler;
    private ModifierHandler modifierHandler;

    public MapHandler(final FishingPlus plugin, final RewardConfiguration rewardConfiguration, final Config config, RewardHandler rewardHandler, ItemHandler itemHandler, ModifierHandler modifierHandler) {
        this.plugin = plugin;
        this.rewardConfiguration = rewardConfiguration;
        this.config = config;
        this.rewardHandler = rewardHandler;
        this.itemHandler = itemHandler;
        this.modifierHandler = modifierHandler;
        getRewardsFromConfig();
        getModifiersFromConfig();
    }

    private void getRewardsFromConfig() {
        ConfigurationSection rewardSection = rewardConfiguration.getRewards();

        if (rewardSection == null) {
            plugin.getLogger().info("The reward config is empty disabling plugin");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }

        for (String key : rewardSection.getKeys(false)) {
            loadNewReward(rewardSection, key);
        }
    }

    private void getModifiersFromConfig() {
        ConfigurationSection modifierSection = rewardConfiguration.getModifiers();

        if (!config.getAllowModifiers() || modifierSection == null)
            return;

        for (String key : modifierSection.getKeys(false)) {
            loadNewModifier(modifierSection, key);
        }
    }

    private void loadNewReward(ConfigurationSection section, String key) {
        try {
            Reward reward;
            String displayName = section.getString(key + ".display-name", key);
            double price = section.getDouble(key + ".price", 0);
            int amount = section.getInt(key + ".item.amount", 1);
            List<String> lore = section.getStringList(key + "item.lore");
            Material material = getRewardMaterial(section, key);
            double chance = getChance(section, key);
            double maxLength = getMaxFishLength(section, key);
            double minLength = getMinFishLength(section, key);

            if (maxLength == 0)
                reward = new Reward(key, chance, price);
            else
                reward = new Fish(key, chance, price, minLength, maxLength);

            rewardHandler.addItemToMap(reward);
            itemHandler.addItemToMap(key, displayName, material, amount, lore);
        } catch (ItemNotFoundException e) {
            Bukkit.getLogger().warning("Could not load the reward " + e.getRewardName()
                    + " because " + e.getItemName() + " is an invalid material");
        } catch (NoChanceToCatchException e) {
            Bukkit.getLogger().warning("There is no chance to catch " + e.getRewardName()
                    + ". Leaving it out of the reward map");
        } catch (InvalidFishLengthException e) {
            Bukkit.getLogger().warning("There was a invalid fish length of " + e.getLength()
                    + " on this fish " + e.getItemName());
        }
    }

    private void loadNewModifier(ConfigurationSection section, String key) {
        try {
            String displayName = getModifierDisplayName(section, key);
            double priceIncrease = section.getDouble(key + ".price-increase", 0);
            double chance = getChance(section, key);

            modifierHandler.addToMap(new Modifier(key, displayName, priceIncrease, chance));
        } catch (NoChanceToCatchException e) {
            Bukkit.getLogger().warning("There is no chance to get the modifier " + e.getRewardName()
                    + ". Leaving it out of the modifier map");
        }
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

    private double getChance(@NotNull ConfigurationSection section, @NotNull String name) throws NoChanceToCatchException {
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
    private String getModifierDisplayName(@NotNull ConfigurationSection section, String key) {
        String displayName = section.getString(key + ".display-name", "");
        if (!displayName.equals(""))
            return config.getModifierPrefix() + displayName + config.getModifierSuffix();
        else
            return "";
    }
}
