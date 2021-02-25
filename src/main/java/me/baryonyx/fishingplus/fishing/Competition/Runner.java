package me.baryonyx.fishingplus.fishing.Competition;

import me.baryonyx.fishingplus.FishingPlus;
import me.baryonyx.fishingplus.configuration.Config;
import me.baryonyx.fishingplus.exceptions.InvalidCompetitionStateException;
import me.baryonyx.fishingplus.fishing.Fish;
import me.baryonyx.fishingplus.fishing.Reward;
import me.baryonyx.fishingplus.handlers.ItemHandler;
import me.baryonyx.fishingplus.handlers.RewardHandler;
import me.baryonyx.fishingplus.messaging.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class Runner {
    private final FishingPlus plugin;
    private Config config;
    private Competition competition;
    private ItemHandler itemHandler;
    private RewardHandler rewardHandler;
    private Messages messages;
    private TimerBar timerBar;

    private TimedStarter timedStarter;


    public Runner(FishingPlus plugin, Config config, Competition competition, ItemHandler itemHandler, RewardHandler rewardHandler, Messages messages, TimerBar timerBar) {
        this.plugin = plugin;
        this.config = config;
        this.competition = competition;
        this.itemHandler = itemHandler;
        this.rewardHandler = rewardHandler;
        this.messages = messages;
        this.timerBar = timerBar;

        timedStarter = new TimedStarter(this, config, plugin);
        timedStarter.autoRunner();
    }

    // Starts a competition with a defined ending if one is not already started
    public void startTimedCompetition(Long time) {
        try {
            competition.startCompetition();
            plugin.getServer().getScheduler().runTaskLater(plugin, this::stopCompetition, time * 20 * 60);
            timerBar.startTimer(time);
            messages.competitionStart(time);
        } catch (InvalidCompetitionStateException e) {
            Bukkit.getLogger().info("Could not start a fishing competition because there is already one running!");
        }
    }

    // Starts a competition with no end if one is not already running
    public void startUndefinedCompetition() {
        try {
            competition.startCompetition();
            messages.competitionStart(0L);
        } catch (InvalidCompetitionStateException e) {
            Bukkit.getLogger().info("Could not start a fishing competition because there is already one running!");
        }
    }

    // Stops a competition if one is running
    public void stopCompetition() {
        try {
            List<Entry> entries = competition.sortCompetitionResults();
            competition.stopCompetition();

            if (timerBar.bar != null) {
                timerBar.removeTimer();
            }

            handleResults(entries);
        } catch (InvalidCompetitionStateException e) {
            Bukkit.getLogger().info("Could not stop the fishing competition because there no competition running!");
        }
    }

    public String getCompetitionStandings(@Nullable List<Entry> entries) {
        if (entries == null) {
            try {
                entries = competition.sortCompetitionResults();
            } catch (InvalidCompetitionStateException e) {
                return null;
            }
        }

        int size = config.getConfigInt("players-displayed");
        int offlinePlayers = 0;
        StringBuilder standings = new StringBuilder();

        if (entries.size() < size) {
            size = entries.size();
        }

        for (int i = 0; i < size; i++) {
            Entry entry = entries.get(i);
            Player player = plugin.getServer().getPlayer(entry.player);

            if (player == null) {
                offlinePlayers++;
                continue;
            }

            String message = config.getMessageString("competition-results")
                    .replace("%ordinal%", Messages.ordinal(i + 1 - offlinePlayers))
                    .replace("%player%", player.getName())
                    .replace("%fish%", entry.fish.name)
                    .replace("%length%", String.valueOf(entry.fish.length));

            standings.append(message);
        }

        return standings.toString();
    }

    // Gives the player a prize and gets their top
    private int givePlayerReward(@NotNull List<Entry> entries, Player player) {
        int rank = 0;
        int prizes = rewardHandler.getCompetitionRewardLength();

        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).player == player.getUniqueId()) {
                rank = i + 1;

                if (i <= prizes) {
                    giveCompetitionReward(player, i + 1);
                }

                break;
            }
        }

        return rank;
    }

    // Sends winners to chat handler and gives rewards
    private void handleResults(List<Entry> entries) {
        if (entries.size() < config.getConfigInt("minimum-fishers")) {
            messages.notEnoughParticipants();
            return;
        }

        String standings = getCompetitionStandings(entries);

        for (Player player : plugin.getServer().getOnlinePlayers()) {
            int rank = givePlayerReward(entries, player);
            messages.competitionResult(standings, rank, player);
        }
    }

    // Gives a player a competition reward
    private void giveCompetitionReward(@NotNull Player player, int i) {
        Reward reward = rewardHandler.getCompetitionReward(i);

        if (reward == null) {
            plugin.getLogger().warning("There was an error giving out the " + i + " reward for a competition");
            return;
        }

        ItemStack item = itemHandler.createRewardItem(reward.name, player.getName(), false);
        rewardHandler.runCommands(reward, player);

        if (item != null) {
            Map<Integer, ItemStack> items = player.getInventory().addItem(item);

            // drops items on floor if the inventory is too full
            for (ItemStack drop : items.values()) {
                player.getWorld().dropItemNaturally(player.getLocation(), drop);
            }
        }
    }

    // Creates an entry for fishing competition
    public void logFish(Player player, ItemStack item) {
        // Canceling if reward is not a fish
        if (!itemHandler.isFish(item)) {
            return;
        }

        String name = itemHandler.getRewardName(item);
        String modifier = itemHandler.getModifierName(item);
        double length = itemHandler.getFishLength(item);

        messages.caughtFish(player, name, modifier, length);
        competition.logFish(player, new Fish(name, length));
    }
}
