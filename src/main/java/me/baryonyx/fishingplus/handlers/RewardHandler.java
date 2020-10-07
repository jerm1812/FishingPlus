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
    private final Config config;
    private final RewardConfiguration rewardConfig;
    private NavigableMap<Double, Reward> rewardMap = new TreeMap<>();
    private final Random random = new Random();
    private double totalWeight = 0;

    public RewardHandler(Config config, RewardConfiguration rewardConfig) {
        this.config = config;
        this.rewardConfig = rewardConfig;
    }

    public void addItemToMap(Reward reward) {
        totalWeight += reward.chance;
        rewardMap.put(totalWeight, reward);
    }

    public Reward getRandomReward() {
        double value = random.nextDouble() * totalWeight;
        return rewardMap.higherEntry(value).getValue();
    }

    @Nullable
    public Reward getReward(String name) {
        for (Reward reward : rewardMap.values()) {
            if (reward.name.equals(name))
                return reward;
        }

        return null;
    }

    public double generateWeightedLength(@NotNull Fish fish) {
        return Math.round((fish.minLength + (fish.maxLength + 1 - fish.minLength) *
                Math.pow(Math.random(), config.getFishLengthWeight())) * 100d) / 100d;
    }
}
