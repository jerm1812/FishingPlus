package me.baryonyx.fishingplus.handlers;

import me.baryonyx.fishingplus.configuration.Config;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import me.baryonyx.fishingplus.fishing.Fish;
import me.baryonyx.fishingplus.fishing.Reward;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

public class RewardHandler {
    private NavigableMap<Double, Reward> fishingRewardMap = new TreeMap<>();
    private Map<String, Reward> competitionRewardMap = new TreeMap<>();
    private final Random random = new Random();
    private double totalWeight = 0;
    private double fishLengthWeight;

    public RewardHandler(@NotNull Config config) {
        fishLengthWeight = config.getConfigDouble("fish-length-weight");
    }

    // Adds a FishingPlus reward to the fishing map
    public void addFishingRewardToMap(@NotNull Reward reward) {
        totalWeight += reward.chance;
        fishingRewardMap.put(totalWeight, reward);
    }

    // Adds a FishingPlus reward to the competition map
    public void addCompetitionRewardToMap(@NotNull Reward reward) {
        competitionRewardMap.put(reward.name, reward);
    }

    // Gets a random reward from the fishing map
    public Reward getRandomFishingReward() {
        double rng = random.nextDouble();
        double value = rng * totalWeight;
        return fishingRewardMap.higherEntry(value).getValue();
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
    public Reward getCompetitionReward(int i) {
        return competitionRewardMap.get(String.valueOf(i));
    }

    public Integer getCompetitionRewardLength() {
        return competitionRewardMap.size();
    }

    // Generates a fish length for a fish
    public double generateWeightedLength(@NotNull Fish fish) {
        return Math.round((fish.minLength + (fish.maxLength + 1 - fish.minLength) *
                Math.pow(Math.random(), fishLengthWeight)) * 100d) / 100d;
    }

    // Runs the commands of the reward
    public void runCommands(Reward reward, Player player) {
        if (!reward.commands.isEmpty()) {
            for (String command : reward.commands) {
                ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
                command = command.replace("%player%", player.getName());
                Bukkit.dispatchCommand(console, command);
            }
        }
    }

    public void clear() {
        totalWeight = 0;
        fishingRewardMap.clear();
        competitionRewardMap.clear();
    }
}
