package me.baryonyx.fishingplus.handlers;

import me.baryonyx.fishingplus.FishingPlus;
import org.bukkit.scheduler.BukkitRunnable;

public class CompetitionHandler extends BukkitRunnable {
    private final FishingPlus plugin;
    public boolean running = false;

    public CompetitionHandler(FishingPlus plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {

    }
    //TODO Implement a competition handler
}
