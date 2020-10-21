package me.baryonyx.fishingplus.fishing.Competition;

import me.baryonyx.fishingplus.FishingPlus;
import me.baryonyx.fishingplus.configuration.Config;
import me.baryonyx.fishingplus.exceptions.InvalidCompetitionStateException;
import me.baryonyx.fishingplus.fishing.Fish;
import me.baryonyx.fishingplus.fishing.Modifier;
import me.baryonyx.fishingplus.fishing.Reward;
import me.baryonyx.fishingplus.handlers.ItemHandler;
import me.baryonyx.fishingplus.handlers.RewardHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Runner {
    private final FishingPlus plugin;
    private Config config;
    private Competition competition;
    private ItemHandler itemHandler;
    private RewardHandler rewardHandler;
    private Announcements announcements;
    private TimerBar timerBar;
    private boolean fiveMinuteWarning = false;


    public Runner(FishingPlus plugin, Config config, Competition competition, ItemHandler itemHandler, RewardHandler rewardHandler, Announcements announcements, TimerBar timerBar) {
        this.plugin = plugin;
        this.config = config;
        this.competition = competition;
        this.itemHandler = itemHandler;
        this.rewardHandler = rewardHandler;
        this.announcements = announcements;
        this.timerBar = timerBar;
    }

    public void startTimedCompetition(Long time) {
        try {
            competition.startCompetition();
            plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, this::stopCompetition, time * 20 * 60);
            timerBar.startTimer(time);

            if (time > 5) {
                //FIXME change it so time is a linked list maybe?
                plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, this::competitionTimeWarning, (time - 5) * 20 * 60);
                plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, this::competitionTimeWarning, (time - 1) * 20 * 60);
            }

            announcements.broadcastCompetitionStart(time);
        } catch (InvalidCompetitionStateException e) {
            Bukkit.getLogger().info("Could not start a fishing competition because there is already one running!");
        }
    }

    // Starts a competition with no end if one is not already running
    public void startUndefinedCompetition() {
        try {
            competition.startCompetition();
            announcements.broadcastCompetitionStart(0);
        } catch (InvalidCompetitionStateException e) {
            Bukkit.getLogger().info("Could not start a fishing competition because there is already one running!");
        }
    }

    // Stops a competition if one is running
    public void stopCompetition() {
        try {
            List<Entry> entries = sortCompetitionMap(competition.getCompetitionStats());
            competition.stopCompetition();
            timerBar.removeTimer();
            announcements.broadcastCompetitionEnd();
            handleResults(entries);
            fiveMinuteWarning = false;
        } catch (InvalidCompetitionStateException e) {
            Bukkit.getLogger().info("Could not stop the fishing competition because there no competition running!");
        }
    }

    // Sorts the competition map
    private List<Entry> sortCompetitionMap(Map<Player, Entry> map) {
        List<Entry> entries = new ArrayList<>(map.values());
        Collections.sort(entries);
        return entries;
    }

    // Sends winners to chat handler and gives rewards
    private void handleResults(List<Entry> entries) {
        if (entries == null || entries.size() < config.getMinimumParticipants()) {
            announcements.broadcastNotEnoughParticipants();
            return;
        }

        int size = config.getAmountOfWinnersDisplayed();
        //FIXME Replace this with an ordinal number function
        String[] sfx = new String[] { "1st", "2nd", "3rd", "4th", "5th", "6th", "7th", "8th", "9th", "10th" };

        // Caps size to competition players
        if (entries.size() < size) {
            size = entries.size();
        }

        // Sends winners to be displayed
        for (int i = 0; i < size; i++) {
            Entry entry = entries.get(i);
            announcements.broadcastCompetitionResults(entry.player.getName(), sfx[i], entry.fish.name, entry.fish.actualLength);
            giveCompetitionReward(entry.player);
        }
    }

    // Gives a player a competition reward
    private void giveCompetitionReward(@NotNull Player player) {
        Reward reward = rewardHandler.getRandomCompetitionReward();
        ItemStack item = itemHandler.createRewardItem(reward.name, player.getName(), false);
        Map<Integer, ItemStack> items = player.getInventory().addItem(item);

        for (ItemStack drop : items.values()) {
            player.getWorld().dropItemNaturally(player.getLocation(), drop);
        }
    }

    private void competitionTimeWarning() {
        //FIXME make this so it is not bad
        if (!fiveMinuteWarning) {
            announcements.broadcastCompetitionTimeLeft(5);
            fiveMinuteWarning = true;
        }
        else {
            announcements.broadcastCompetitionTimeLeft(1);
        }
    }

    // Creates an entry for fishing competition
    public void logFish(Player player, ItemStack item) {
        // Canceling if reward is not a fish
        if (!itemHandler.isFish(item))
            return;

        String name = itemHandler.getRewardName(item);
        double length = itemHandler.getFishLength(item);

        competition.logFish(player, new Fish(name, length));
    }
}
