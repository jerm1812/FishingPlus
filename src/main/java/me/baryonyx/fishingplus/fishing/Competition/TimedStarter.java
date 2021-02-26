package me.baryonyx.fishingplus.fishing.Competition;

import me.baryonyx.fishingplus.FishingPlus;
import me.baryonyx.fishingplus.configuration.Config;
import org.bukkit.Bukkit;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class TimedStarter {
    private Runner runner;
    private Config config;
    private FishingPlus plugin;

    private List<LocalTime> runTimes = new ArrayList<>();

    public TimedStarter(Runner runner, Config config, FishingPlus plugin) {
        this.runner = runner;
        this.config = config;
        this.plugin = plugin;
    }

    // Sets up the competition run times
    void autoRunner() {
        for (String string : config.getConfigStringList("competition-run-times")) {
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
            long duration = config.getConfigInt("competition-duration");
            runner.startTimedCompetition(duration);
        }
    }
}
