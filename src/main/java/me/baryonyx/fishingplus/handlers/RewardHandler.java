package me.baryonyx.fishingplus.handlers;

import me.baryonyx.fishingplus.configuration.Config;
import me.baryonyx.fishingplus.configuration.RewardConfiguration;
import me.baryonyx.fishingplus.fishing.Modifier;
import org.jetbrains.annotations.NotNull;
import me.baryonyx.fishingplus.fishing.Fish;
import me.baryonyx.fishingplus.fishing.Reward;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Nullable;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

public class RewardHandler {
    private NavigableMap<Double, Reward> rewardMap = new TreeMap<>();
    private final Random random = new Random();
    private double totalWeight = 0;
    private double fishLengthWeight;

    public RewardHandler(@NotNull Config config) {
        fishLengthWeight = config.getFishLengthWeight();
    }

    // Adds a FishingPlus reward to the map
    void addRewardToMap(@NotNull Reward reward) {
        totalWeight += reward.chance;
        rewardMap.put(totalWeight, reward);
    }

    // Gets a random reward from the map
    Reward getRandomReward() {
        double value = random.nextDouble() * totalWeight;
        return rewardMap.higherEntry(value).getValue();
    }

    // Gets a specific reward from the map
    @Nullable
    public Reward getReward(String name) {
        for (Reward reward : rewardMap.values()) {
            if (reward.name.equals(name))
                return reward;
        }

        return null;
    }

    // Generates a fish length for a fish
    double generateWeightedLength(@NotNull Fish fish) {
        return Math.round((fish.minLength + (fish.maxLength + 1 - fish.minLength) *
                Math.pow(Math.random(), fishLengthWeight)) * 100d) / 100d;
    }
}
