package me.baryonyx.fishingplus.handlers;

import me.baryonyx.fishingplus.FishingPlus;
import me.baryonyx.fishingplus.configuration.Config;
import me.baryonyx.fishingplus.configuration.RewardConfiguration;
import me.baryonyx.fishingplus.exceptions.InvalidFishLengthException;
import me.baryonyx.fishingplus.exceptions.ItemNotFoundException;
import me.baryonyx.fishingplus.exceptions.NoChanceToCatchException;
import me.baryonyx.fishingplus.exceptions.NoFishInModifierException;
import me.baryonyx.fishingplus.fishing.Fish;
import me.baryonyx.fishingplus.fishing.Modifier;
import me.baryonyx.fishingplus.fishing.Reward;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CatchHandler {
    private final FishingPlus plugin;
    private final RewardConfiguration rewardConfiguration;
    private final Config config;
    private RewardHandler rewardHandler;
    private ItemHandler itemHandler;
    private ModifierHandler modifierHandler;

    public CatchHandler(final FishingPlus plugin, final RewardConfiguration rewardConfiguration, final Config config, RewardHandler rewardHandler, ItemHandler itemHandler, ModifierHandler modifierHandler) {
        this.plugin = plugin;
        this.rewardConfiguration = rewardConfiguration;
        this.config = config;
        this.rewardHandler = rewardHandler;
        this.itemHandler = itemHandler;
        this.modifierHandler = modifierHandler;
        getFishingRewardsFromConfig();
        getCompetitionRewardsFromConfig();
        getModifiersFromConfig();
    }

    // Gets the fishing rewards from the rewards file and disables the plugin if there are no rewards
    private void getFishingRewardsFromConfig() {
        ConfigurationSection rewardSection = rewardConfiguration.getFishingRewards();

        if (rewardSection != null) {

            // Sends each reward section to be loaded
            for (String key : rewardSection.getKeys(false)) {
                loadRewardToMaps(rewardSection, key, true);
            }

            return;
        }

        Bukkit.getLogger().info("The reward config is empty disabling plugin");
        Bukkit.getServer().getPluginManager().disablePlugin(plugin);
    }

    // Gets the competition rewards from the rewards file
    private void getCompetitionRewardsFromConfig() {
        ConfigurationSection rewardSection = rewardConfiguration.getCompetitionRewards();

        if (rewardSection != null) {
            // Sends each reward section to be loaded
            for (String key : rewardSection.getKeys(false)) {
                loadRewardToMaps(rewardSection, key, false);
            }
        }
    }

    // Gets the modifiers from the rewards file
    private void getModifiersFromConfig() {
        ConfigurationSection modifierSection = rewardConfiguration.getModifiers();

        if (!config.getAllowModifiers() || modifierSection == null)
            return;

        for (String key : modifierSection.getKeys(false)) {
            loadModifierToMap(modifierSection, key);
        }
    }

    // Loads a reward into the item handler and reward handler maps
    private void loadRewardToMaps(@NotNull ConfigurationSection section, String key, boolean isFishingReward ) {
        try {
            Reward reward;
            String displayName = getRewardDisplayName(section, key);
            int amount = section.getInt(key + ".item.amount", 1);
            int customModelData = section.getInt(key + ".item.custom-model-data", 0);
            List<String> lore = section.getStringList(key + ".item.lore");
            List<String> commands = section.getStringList(key + ".commands");
            Material material = getRewardMaterial(section, key);
            double chance = getCatchChance(section, key, isFishingReward);
            double maxLength = getMaxFishLength(section, key);
            double minLength = getMinFishLength(section, key);

            if (maxLength == 0) {
                reward = new Reward(key, chance, customModelData, commands);
            }
            else {
                reward = new Fish(key, chance, customModelData, commands, minLength, maxLength);
            }

            // Adds the reward to the correct map
            if (isFishingReward) {
                rewardHandler.addFishingRewardToMap(reward);
            }
            else {
                rewardHandler.addCompetitionRewardToMap(reward, key);
            }

            // Adds the item to the item map
            itemHandler.addItemToMap(key, displayName, material, amount, lore, customModelData);
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

    // Loads a modifier to the modifier handler map
    private void loadModifierToMap(ConfigurationSection section, String key) {
        try {
            String displayName = getModifierDisplayName(section, key);
            double priceIncrease = section.getDouble(key + ".price-increase", 0);
            double chance = getCatchChance(section, key, true);
            List<String> modifiersFish = getModifiersFish(section, key);

            // Adds the list of possible modifiers to a reward
            for (String name : modifiersFish) {
                Reward reward = rewardHandler.getFishingReward(name);

                if (reward != null && name != null) {
                    reward.modifiers.add(key);
                }
            }

            modifierHandler.addToMap(new Modifier(key, displayName, priceIncrease, chance));
        } catch (NoChanceToCatchException e) {
            Bukkit.getLogger().warning("There is no chance to get the modifier " + e.getRewardName()
                    + ". Leaving it out of the modifier map");
        } catch (NoFishInModifierException e) {
            Bukkit.getLogger().warning("This is no chance to get the modifier " + e.getModifier() + " because it has no fish." +
                    " Leaving it out of the modifier map");
        }
    }

    // Gets a reward's display name
    private String getRewardDisplayName(@NotNull ConfigurationSection section, @NotNull String name) {
        String displayName = config.getRewardNames();
        String rewardName = section.getString(name + ".display-name", name);
        return displayName.replace("%reward-name%", rewardName);
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

    // Gets a reward's chance to be caught
    private double getCatchChance(@NotNull ConfigurationSection section, @NotNull String name, boolean isFishingReward) throws NoChanceToCatchException {
        double chance = section.getDouble(name + ".chance");

        if (chance == 0 && isFishingReward) {
            throw new NoChanceToCatchException(name);
        }

        return chance;
    }

    // Gets a reward's item material from a yaml section
    @NotNull
    private Material getRewardMaterial(@NotNull ConfigurationSection section, @NotNull String name) throws ItemNotFoundException {
        String itemName = section.getString(name + ".item.id");

        if (itemName == null) {
            throw new ItemNotFoundException(null, name);
        }

        Material material = Material.matchMaterial(itemName);

        if (material == null) {
            throw new ItemNotFoundException(itemName, name);
        }

        return material;
    }


    // Gets a modifier's display name from a yaml section
    @NotNull
    private String getModifierDisplayName(@NotNull ConfigurationSection section, String key) {
        String displayName = section.getString(key + ".display-name", "");
        if (displayName != null && !displayName.equals("")) {
            return config.getModifierNames().replace("%modifier-name%", displayName);
        }

        return "";
    }

    private List<String> getModifiersFish(@NotNull ConfigurationSection section, String key) throws NoFishInModifierException {
        List<String> modifiersFish = section.getStringList(key + ".fish");

        if (modifiersFish.isEmpty()) {
            throw new NoFishInModifierException(key);
        }

        return modifiersFish;
    }

    // Replaces caught fish with a FishingPlus reward
    @Nullable
    public ItemStack handleCatchEvent(Player player) {
        Reward reward = rewardHandler.getRandomFishingReward();
        Modifier modifier = modifierHandler.getRandomPossibleModifier(reward);
        rewardHandler.runCommands(reward, player);
        ItemStack item;

        if (reward instanceof Fish) {
            item = itemHandler.createFishItem(reward.name, player.getName(), rewardHandler.generateWeightedLength((Fish)reward));
        }
        else {
            item = itemHandler.createRewardItem(reward.name, player.getName(), true);
        }

        if (item != null && modifier != null) {
            itemHandler.addModifierToItem(item, modifier.displayName);
        }

        return item;
    }
}
