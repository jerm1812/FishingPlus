package me.baryonyx.fishingplus.handlers;

import me.baryonyx.fishingplus.FishingPlus;
import me.baryonyx.fishingplus.configuration.Config;
import me.baryonyx.fishingplus.exceptions.InvalidCompetitionStateException;
import me.baryonyx.fishingplus.fishing.Competition.Competition;
import me.baryonyx.fishingplus.fishing.Competition.Entry;
import me.baryonyx.fishingplus.fishing.Fish;
import me.baryonyx.fishingplus.fishing.Modifier;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class CompetitionHandler {
    private final FishingPlus plugin;
    private Config config;
    private Competition competition;
    private ItemHandler itemHandler;
    private ChatHandler chatHandler;
    private boolean fiveMinuteWarning = false;


    public CompetitionHandler(FishingPlus plugin, Config config, Competition competition, ItemHandler itemHandler, ChatHandler chatHandler) {
        this.plugin = plugin;
        this.config = config;
        this.competition = competition;
        this.itemHandler = itemHandler;
        this.chatHandler = chatHandler;
    }

    public void startTimedCompetition(Long time) {
        try {
            endTime = System.currentTimeMillis() / 1000 + time * 60;
            competition.startCompetition();
            plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, this::stopCompetition, time * 20 * 60);

            if (time > 5) {
                //FIXME change it so time is a linked list maybe?
                plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, this::competitionTimeWarning, (time - 5) * 20 * 60);
                plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, this::competitionTimeWarning, (time - 1) * 20 * 60);
            }

            chatHandler.broadcastCompetitionStart(time);
        } catch (InvalidCompetitionStateException e) {
            Bukkit.getLogger().info("Could not start a fishing competition because there is already one running!");
        }
    }

    // Starts a competition with no end if one is not already running
    public void startUndefinedCompetition() {
        try {
            competition.startCompetition();
            chatHandler.broadcastCompetitionStart(0);
        } catch (InvalidCompetitionStateException e) {
            Bukkit.getLogger().info("Could not start a fishing competition because there is already one running!");
        }
    }

    // Stops a competition if one is running
    public void stopCompetition() {
        try {
            List<Entry> entries = sortCompetitionMap(competition.getCompetitionStats());
            competition.stopCompetition();
            chatHandler.broadcastCompetitionEnd();
            handleResults(entries);
            fiveMinuteWarning = false;
        } catch (InvalidCompetitionStateException e) {
            Bukkit.getLogger().info("Could not stop the fishing competition because there no competition running!");
        }
    }

    private List<Entry> sortCompetitionMap(Map<Player, Entry> map) {
        List<Entry> entries = new ArrayList<>(map.values());
        Collections.sort(entries);
        return entries;
    }

    // Sends winners to chat handler and gives rewards
    private void handleResults(List<Entry> entries) {
        if (entries == null) {
            return;
        }

        int size = config.getAmountOfWinnersDisplayed();
        String[] sfx = new String[] { "1st", "2nd", "3rd", "4th", "5th", "6th", "7th", "8th", "9th", "10th" };

        // Caps size to competition players
        if (entries.size() < size) {
            size = entries.size();
        }

        // Sends winners to be displayed
        for (int i = 0; i < size; i++) {
            Entry entry = entries.get(i);
            chatHandler.broadcastCompetitionResults(entry.player.getName(), sfx[i], entry.fish.name, entry.fish.actualLength);
        }
    }

    private void competitionTimeWarning() {
        //FIXME make this so it is not bad
        if (!fiveMinuteWarning) {
            chatHandler.broadcastCompetitionTimeLeft(5);
            fiveMinuteWarning = true;
        }
        else {
            chatHandler.broadcastCompetitionTimeLeft(1);
        }
    }

    // Creates an entry for fishing competition
    public void logFish(Player player, ItemStack item) {
        // Canceling if reward is not a fish
        if (!itemHandler.isFish(item))
            return;

        String name = itemHandler.getRewardName(item);
        String modifier = itemHandler.getModifierName(item);
        double length = itemHandler.getFishLength(item);

        competition.logFish(player, new Fish(name, length), new Modifier(modifier));
    }
}
