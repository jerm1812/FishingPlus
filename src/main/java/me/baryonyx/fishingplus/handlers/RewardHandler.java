package me.baryonyx.fishingplus.handlers;

import me.baryonyx.fishingplus.configuration.Config;
import org.jetbrains.annotations.NotNull;
import me.baryonyx.fishingplus.fishing.Fish;
import me.baryonyx.fishingplus.fishing.Reward;
import org.jetbrains.annotations.Nullable;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

public class RewardHandler {
    private NavigableMap<Double, Reward> fishingRewardMap = new TreeMap<>();
    private NavigableMap<Double, Reward> competitionRewardMap = new TreeMap<>();
    private final Random random = new Random();
    private double totalFishingWeight = 0;
    private double totalCompetitionWeight = 0;
    private double fishLengthWeight;

    public RewardHandler(@NotNull Config config) {
        fishLengthWeight = config.getFishLengthWeight();
    }

    // Adds a FishingPlus reward to the fishing map
    void addFishingRewardToMap(@NotNull Reward reward) {
        totalFishingWeight += reward.chance;
        fishingRewardMap.put(totalFishingWeight, reward);
    }

    // Adds a FishingPlus reward to the competition map
    void addCompetitionRewardToMap(@NotNull Reward reward) {
        totalCompetitionWeight += reward.chance;
        competitionRewardMap.put(totalCompetitionWeight, reward);
    }

    // Gets a random reward from the fishing map
    Reward getRandomFishingReward() {
        double value = random.nextDouble() * totalFishingWeight;
        return fishingRewardMap.higherEntry(value).getValue();
    }

    // Gets a random reward from the competition map
    public Reward getRandomCompetitionReward() {
        double value = random.nextDouble() * totalCompetitionWeight;
        return competitionRewardMap.higherEntry(value).getValue();
    }

    // Gets a specific reward from the fishing map
    @Nullable
    public Reward getFishingReward(String name) {
        for (Reward reward : fishingRewardMap.values()) {
            if (reward.name.equals(name))
                return reward;
        }

        return null;
    }

    // Gets a specific reward from the competition map
    @Nullable
    public Reward getCompetitionReward(String name) {
        for (Reward reward : competitionRewardMap.values()) {
            if (reward.name.equals(name)) {
                return reward;
            }
        }

        return null;
    }

    // Generates a fish length for a fish
    double generateWeightedLength(@NotNull Fish fish) {
        return Math.round((fish.minLength + (fish.maxLength + 1 - fish.minLength) *
                Math.pow(Math.random(), fishLengthWeight)) * 100d) / 100d;
    }


}
