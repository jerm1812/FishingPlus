package me.baryonyx.fishingplus.configuration;

import me.baryonyx.fishingplus.FishingPlus;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.time.LocalTime;
import java.util.List;

public class Config {
    private FileConfiguration config;

    public Config(@NotNull FishingPlus plugin) {
        config = plugin.getConfig();
    }

    public double getFishLengthWeight() {
        return config.getDouble("fish-length-weight");
    }

    public boolean getDisplayWhoCaught() {
        return config.getBoolean("display-who-caught");
    }

    public String getRewardNames() {
        return config.getString("reward-names");
    }

    public boolean getAllowModifiers() {
        return config.getBoolean("enable-modifiers");
    }

    public String getModifierNames() {
        return config.getString("modifier-names");
    }

    public boolean rewardsOnlyDuringCompetition() {
        return config.getBoolean("competition-only");
    }

    public boolean isBroadcastStartEnabled() {
        return config.getBoolean("broadcast-competition-start");
    }

    public boolean isBroadcastEndEnabled() {
        return config.getBoolean("broadcast-competition-end");
    }

    public String getBroadcastPrefix() {
        return config.getString("message-prefix");
    }

    public int getAmountOfWinnersDisplayed() {
        return config.getInt("players-displayed");
    }

    public int getMinimumParticipants() {
        return config.getInt("minimum-fishers");
    }

    public int getCompetitionDuration() {
        return config.getInt("competition-duration");
    }

    public List<String> getCompetitionRunTimes() {
        return config.getStringList("competition-run-times");
    }

    public double getPriceMultiplier() {
        return config.getDouble("price-multiplier");
    }

    public String getShopSellMessage() {
        return config.getString("sell-message");
    }

    public String getShopName() {
        return config.getString("shop-name");
    }
}
