package me.baryonyx.fishingplus.handlers;

import me.baryonyx.fishingplus.FishingPlus;
import me.baryonyx.fishingplus.exceptions.InvalidCompetitionStateException;
import me.baryonyx.fishingplus.fishing.Competition;
import me.baryonyx.fishingplus.fishing.Fish;
import me.baryonyx.fishingplus.fishing.Modifier;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class CompetitionHandler {
    private final FishingPlus plugin;
    private Competition competition;
    private ItemHandler itemHandler;
    private ChatHandler chatHandler;
    private BukkitTask competitionTimer;
    private Long endTime;


    public CompetitionHandler(FishingPlus plugin, Competition competition, ItemHandler itemHandler, ChatHandler chatHandler) {
        this.plugin = plugin;
        this.competition = competition;
        this.itemHandler = itemHandler;
        this.chatHandler = chatHandler;
    }

    public void startTimedCompetition(Long time) {
        try {
            endTime = System.currentTimeMillis() / 1000 + time * 60;
            competition.startCompetition();
            competitionTimer = plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, this::stopCompetition, time * 20 * 60);

            if (time > 5)
                plugin.getServer().getScheduler().runTaskAsynchronously(plugin, this::displayCompetitionTime);
            
            chatHandler.broadcastCompetitionStart(time * 20 * 60);
        } catch (InvalidCompetitionStateException e) {
            Bukkit.getLogger().info("Could not start a fishing competition because there is already one running!");
        }
    }

    public void startUndefinedCompetition() {
        try {
            competition.startCompetition();
            chatHandler.broadcastCompetitionStart(0);
        } catch (InvalidCompetitionStateException e) {
            Bukkit.getLogger().info("Could not start a fishing competition because there is already one running!");
        }
    }

    public void stopCompetition() {
        try {
            competition.stopCompetition();
            chatHandler.broadcastCompetitionEnd();
        } catch (InvalidCompetitionStateException e) {
            Bukkit.getLogger().info("Could not stop the fishing competition because there no competition running!");
        }
    }

    private void displayCompetitionTime() {
        throw new NotImplementedException();

        while (true) {
            long elapsed = System.currentTimeMillis() / 1000 - competition.startTime;
            timeLeft = elapsed;
            if time
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

    public boolean isCompetitionRunning() {
        return competition.isRunning();
    }

    //TODO Implement a competition handler
}
