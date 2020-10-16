package me.baryonyx.fishingplus.handlers;

import me.baryonyx.fishingplus.FishingPlus;
import me.baryonyx.fishingplus.exceptions.InvalidCompetitionStateException;
import me.baryonyx.fishingplus.fishing.Competition;
import me.baryonyx.fishingplus.fishing.Entry;
import me.baryonyx.fishingplus.fishing.Fish;
import me.baryonyx.fishingplus.fishing.Modifier;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class CompetitionHandler {
    private final FishingPlus plugin;
    private Competition competition;
    private ItemHandler itemHandler;
    private ChatHandler chatHandler;
    private boolean fiveMinuteWarning = false;


    public CompetitionHandler(FishingPlus plugin, Competition competition, ItemHandler itemHandler, ChatHandler chatHandler) {
        this.plugin = plugin;
        this.competition = competition;
        this.itemHandler = itemHandler;
        this.chatHandler = chatHandler;
    }

    public void startTimedCompetition(Long time) {
        try {
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
            Map<Player, Entry> map = competition.getCompetitionStats();
            competition.stopCompetition();

            chatHandler.broadcastCompetitionEnd();
            fiveMinuteWarning = false;
        } catch (InvalidCompetitionStateException e) {
            Bukkit.getLogger().info("Could not stop the fishing competition because there no competition running!");
        }
    }

    private void getTopThree() {

    }

    private void competitionTimeWarning() {
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
