package me.baryonyx.fishingplus.fishing.Competition;

import me.baryonyx.fishingplus.FishingPlus;
import me.baryonyx.fishingplus.configuration.Config;
import me.baryonyx.fishingplus.exceptions.InvalidCompetitionStateException;
import me.baryonyx.fishingplus.fishing.Fish;
import me.baryonyx.fishingplus.fishing.Reward;
import me.baryonyx.fishingplus.handlers.ItemHandler;
import me.baryonyx.fishingplus.handlers.RewardHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.*;

public class Runner {
    private final FishingPlus plugin;
    private Config config;
    private Competition competition;
    private ItemHandler itemHandler;
    private RewardHandler rewardHandler;
    private Announcements announcements;
    private TimerBar timerBar;
    private List<LocalTime> runTimes = new ArrayList<>();


    public Runner(FishingPlus plugin, Config config, Competition competition, ItemHandler itemHandler, RewardHandler rewardHandler, Announcements announcements, TimerBar timerBar) {
        this.plugin = plugin;
        this.config = config;
        this.competition = competition;
        this.itemHandler = itemHandler;
        this.rewardHandler = rewardHandler;
        this.announcements = announcements;
        this.timerBar = timerBar;

        autoRunner();
    }

    // Starts a competition with a defined ending if one is not already started
    public void startTimedCompetition(Long time) {
        try {
            competition.startCompetition();
            plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, this::stopCompetition, time * 20 * 60);
            timerBar.startTimer(time);
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

            if (timerBar.bar != null) {
                timerBar.removeTimer();
            }

            announcements.broadcastCompetitionEnd();
            handleResults(entries);
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

        // Caps size to competition players
        if (entries.size() < size) {
            size = entries.size();
        }

        // Sends winners to be displayed
        for (int i = 0; i < size; i++) {
            Entry entry = entries.get(i);
            announcements.broadcastCompetitionResults(entry.player.getName(), ordinal(i + 1), entry.fish.name, entry.fish.actualLength);
            if (i < rewardHandler.getCompetitionRewardLength()) {
                giveCompetitionReward(entry.player, i + 1);
            }
        }
    }

    // Returns the ordinal of a number
    private String ordinal(int i)  {
        String[] suffixes = new String[] { "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th" };
        switch (i % 100) {
            case 11:
            case 12:
            case 13:
                return i + "th";
            default:
                return i + suffixes[i % 10];

        }
    }

    // Gives a player a competition reward
    private void giveCompetitionReward(@NotNull Player player, int i) {
        Reward reward = rewardHandler.getCompetitionReward(i);
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
        double length = itemHandler.getFishLength(item);

        announcements.messagePlayerCaughtFish(player, name, length);
        competition.logFish(player, new Fish(name, length));
    }

    // Sets up the competition run times
    private void autoRunner() {
        for (String string : config.getCompetitionRunTimes()) {
            try {
                LocalTime time = LocalTime.parse(string);
                runTimes.add(time);
            } catch (DateTimeParseException e) {
                Bukkit.getLogger().warning("Could not add the time: " + e.getParsedString() + " to the Fishing Competition auto runner");
            }
        }

        // Checks time to run every 45 seconds
        plugin.getServer().getScheduler().runTaskTimer(plugin, this::run, 0, 900);
    }

    // Runs a fishing competition at the defined times of day
    private void run() {
        if (runTimes.contains(LocalTime.now().withSecond(0).withNano(0))) {
            long duration = config.getCompetitionDuration();
            startTimedCompetition(duration);
        }
    }
}
