package me.baryonyx.fishingplus.fishing;

import me.baryonyx.fishingplus.configuration.Config;
import me.baryonyx.fishingplus.exceptions.RewardNotFoundException;
import me.baryonyx.fishingplus.handlers.RewardHandler;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class FishingMap {
    private Config config;
    private RewardHandler rewardHandler;
    private NavigableMap<Double, Reward> rewardMap = new TreeMap<>();
    private final Random random;
    private double rewardWeight = 0;

    public FishingMap(Config config, RewardHandler rewardHandler) {
        this.config = config;
        this.rewardHandler = rewardHandler;
        this.random = new Random();
        loadRewardMap();
        loadModifierMap();
    }

    private void loadRewardMap() {
        ConfigurationSection section = config.getRewards();

        for (String name: section.getKeys(false)) {
            Reward reward = rewardHandler.loadNewReward(section, name);

            if (reward == null)
                continue;

            addToRewardMap(reward.chance, reward);
        }
    }

    private void loadModifierMap() {
        ConfigurationSection section = config.getModifiers();

        for (String name : section.getKeys(false)) {
            Modifier modifier = rewardHandler.loadNewModifier(section, name);

            if (modifier == null)
                continue;

            addToModifierMap(modifier);
        }
    }

    private void addToModifierMap(@NotNull Modifier modifier) {
        if (modifier.chance > 0) {
            modifierWeight += modifier.chance;
            modifierMap.put(modifierWeight, modifier);
        }
    }

    private void addToRewardMap(double weight, @NotNull Reward reward) {
        if (weight > 0) {
            rewardWeight += weight;
            rewardMap.put(rewardWeight, reward);
        }
    }

    @NotNull
    private Reward getRandomReward(@NotNull Player player) throws RewardNotFoundException {
        double value = random.nextDouble() * rewardWeight;
        Reward reward = rewardMap.higherEntry(value).getValue();

        if (reward == null)
            throw new RewardNotFoundException(player.getName());

        return reward;
    }

    @Nullable
    public Reward getReward(String name) {
        for (Reward reward : rewardMap.values())
            if (reward.name.equals(name)) {
                if (reward instanceof Fish)
                    return new Fish(reward.name, reward.price);
                else
                    return new Reward(reward.name, reward.price);
            }

        return null;
    }

    @Nullable
    public ItemStack createReward(@NotNull Player player) {
        try {
            Reward reward = getRandomReward(player);
            Modifier modifier = null;

            if (config.getAllowModifiers())
                modifier = getRandomModifier();

            return rewardHandler.createRewardItem(player, reward, modifier);
        }
        catch (RewardNotFoundException e) {
            Bukkit.getLogger().severe("There was an error getting a reward from the reward rewardMap. The"
                    + " player fishing was " + e.getPlayer());
        }

        return null;
    }
}
