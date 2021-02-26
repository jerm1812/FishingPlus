package me.baryonyx.fishingplus.fishing;

import me.baryonyx.fishingplus.FishingPlus;
import me.baryonyx.fishingplus.configuration.Config;
import me.baryonyx.fishingplus.exceptions.InvalidFishLengthException;
import me.baryonyx.fishingplus.exceptions.ItemNotFoundException;
import me.baryonyx.fishingplus.handlers.ItemHandler;
import me.baryonyx.fishingplus.handlers.ModifierHandler;
import me.baryonyx.fishingplus.handlers.RewardHandler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class RewardLoader {
    private FishingPlus plugin;
    private Config config;
    private RewardHandler rewardHandler;
    private ModifierHandler modifierHandler;
    private ItemHandler itemHandler;

    public RewardLoader(FishingPlus plugin, Config config, RewardHandler rewardHandler, ModifierHandler modifierHandler, ItemHandler itemHandler) {
        this.plugin = plugin;
        this.config = config;
        this.rewardHandler = rewardHandler;
        this.modifierHandler = modifierHandler;
        this.itemHandler = itemHandler;

        load();
    }

    public void reload() {
        rewardHandler.clear();
        modifierHandler.clear();
        itemHandler.clear();
        config.reloadRewards();
        load();
    }

    private void load() {
        loadFishingRewards();
        loadCompetitionRewards();
        loadModifiers();
    }

    private void loadFishingRewards() {
        List<ConfigurationSection> sections = config.getRewardSections("fishing-rewards");

        if (sections == null || sections.size() == 0) {
            Bukkit.getLogger().info("The reward configs are empty or has an error. Please fix this and then reloadConfig with /fp reload rewards");
            return;
        }

        String displayName = config.getConfigString("reward-names");

        for (ConfigurationSection section : sections) {
            // Sends each reward section to be loaded
            for (String key : section.getKeys(false)) {
                Reward reward = getRewardFromFile(section, key, displayName);

                if (reward == null) {
                    continue;
                }

                rewardHandler.addFishingRewardToMap(reward);
                addItems(section, key, reward.displayName);
            }
        }
    }

    private void loadCompetitionRewards() {
        ConfigurationSection section = config.getCompetitionRewardSection("competition-rewards");

        if (section == null) {
            Bukkit.getLogger().info("The competition rewards are empty or has an error. Please fix this and then reloadConfig with /fp reloadConfig rewards");
            return;
        }

        String displayName = config.getConfigString("reward-names");

        for (String key : section.getKeys(false)) {
            Reward reward = getRewardFromFile(section, key, displayName);

            if (reward == null) {
                continue;
            }

            rewardHandler.addCompetitionRewardToMap(reward);
            addItems(section, key, reward.displayName);
        }
    }

    private void loadModifiers() {
        List<ConfigurationSection> sections = config.getRewardSections("modifiers");

        if (!config.getConfigBool("enable-modifiers") || sections == null || sections.size() == 0) {
            return;
        }

        String displayName = config.getConfigString("modifier-names");

        for (ConfigurationSection section : sections) {
            for (String key : section.getKeys(false)) {
                Modifier modifier = getModifierFromFile(section, key, displayName);
                addModifierToFish(modifier.name, modifier.fish);
                modifierHandler.addToMap(modifier);
            }
        }
    }

    private Reward getRewardFromFile(ConfigurationSection section, String key, String displayName) {
        Reward reward;

        try {
            // Gets attributes
            String name = getRewardDisplayName(section, key, displayName);
            List<String> commands = section.getStringList(key + ".commands");
            double chance = section.getDouble(key + ".chance");
            double maxLength = getMaxFishLength(section, key);
            double minLength = getMinFishLength(section, key);

            // Creates fish or reward
            if (maxLength == 0) {
                reward = new Reward(key, name, chance, commands);
            } else {
                reward = new Fish(key, name, chance, commands, minLength, maxLength);
            }

            return reward;
        } catch (InvalidFishLengthException e) {
            plugin.getLogger().info(String.format("Could not load %s because it had an invalid length.", key));
        }

        return null;
    }

    // Returns a modifier from the file
    private Modifier getModifierFromFile(ConfigurationSection section, String key, String displayName) {
        // Gets attributes
        String name = getRewardDisplayName(section, key, displayName);
        double priceIncrease = section.getDouble(key + ".price-increase");
        double chance = section.getDouble(key + ".chance");
        List<String> fish = section.getStringList(key + ".fish");

        return new Modifier(key, name, priceIncrease, chance, fish);
    }

    private void addItems(ConfigurationSection section, String key, String displayName) {
        Material material;

        try {
            material = getRewardMaterial(section, key);
        } catch (ItemNotFoundException e) {
            Bukkit.getLogger().warning("Could not load the reward " + e.getRewardName()
                    + " because " + e.getItemName() + " is an invalid material");
            return;
        }

        if (material == null) {
            return;
        }

        String name = getRewardDisplayName(section, key, displayName);
        int amount = section.getInt(key + ".item.amount", 1);
        int modelData = section.getInt(key + ".item.custom-model-data");
        int potionColor = section.getInt(key + ".item.custom-potion-color");
        List<String> lore = section.getStringList(key + ".item.lore");
        List<PotionEffect> effects = new ArrayList<>();

        if (material == Material.POTION) {
            effects = getPotionEffects(section, key);
        }

        itemHandler.addItemToMap(key, name, material, amount, lore, modelData, effects, potionColor);
    }

    // Gets a reward's display name
    private String getRewardDisplayName(@NotNull ConfigurationSection section, @NotNull String name, String displayName) {
        String rewardName = section.getString(name + ".display-name");
        return rewardName == null ? displayName : displayName.replace("%display-name%", rewardName);
    }

    // Gets a fish's minimum length from a yaml section
    private double getMinFishLength(@NotNull ConfigurationSection section, @NotNull String name) throws InvalidFishLengthException {
        double minLength = section.getDouble(name + ".length.min");

        if (minLength < 0) {
            throw new InvalidFishLengthException(name, minLength);
        }

        return minLength;
    }

    // Gets a fish's maximum length from a yaml section
    private double getMaxFishLength(@NotNull ConfigurationSection section, @NotNull String name) throws InvalidFishLengthException {
        double maxLength = section.getDouble(name + ".length.max");

        if (maxLength < 0) {
            throw new InvalidFishLengthException(name, maxLength);
        }

        return maxLength;
    }

    // Gets a reward's item material from a yaml section
    private Material getRewardMaterial(@NotNull ConfigurationSection section, @NotNull String name) throws ItemNotFoundException {
        String itemName = section.getString(name + ".item.id");

        if (itemName == null) {
            return null;
        }

        Material material = Material.matchMaterial(itemName);

        if (material == null) {
            throw new ItemNotFoundException(itemName, name);
        }

        return material;
    }

    private List<PotionEffect> getPotionEffects(@NotNull ConfigurationSection section, @NotNull String name) {
        List<PotionEffect> effects = new ArrayList<>();
        section = section.getConfigurationSection(name + ".item.custom-potion-effects");

        if (section == null) {
            return effects;
        }

        for (String key : section.getKeys(false)) {
            String type = section.getString(key + ".id");

            if (type == null) {
                continue;
            }

            PotionEffectType potionEffectType = PotionEffectType.getByName(type);

            if (potionEffectType == null) {
                continue;
            }

            int duration = section.getInt(key + ".duration");
            int amplifier = section.getInt(key + ".amplifier");
            effects.add(new PotionEffect(potionEffectType, duration, amplifier));
        }

        return effects;
    }

    private void addModifierToFish(String modifier, List<String> fish) {
        for (String name : fish) {
            Reward reward = rewardHandler.getFishingReward(name);

            if (reward != null && name != null) {
                reward.modifiers.add(modifier);
            }
        }
    }
}
