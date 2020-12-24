package me.baryonyx.fishingplus.fishing.Competition;

import me.baryonyx.fishingplus.FishingPlus;
import me.baryonyx.fishingplus.configuration.Config;
import me.baryonyx.fishingplus.listener.CompetitionListener;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.scheduler.BukkitTask;

public class TimerBar {
    private FishingPlus plugin;
    private Config config;
    private NamespacedKey key;
    public BossBar bar = null;
    private BukkitTask updater;
    private Long timeLeft;
    private Long totalTime;

    private CompetitionListener competitionListener = new CompetitionListener(this);


    public TimerBar(FishingPlus plugin, Config config) {
        this.plugin = plugin;
        this.config = config;

        key = new NamespacedKey(plugin, "competition-timer");
    }

    // Starts a boss bar timer
    void startTimer(Long time) {
        // Creating the time bar
        bar = plugin.getServer().createBossBar(key, "", BarColor.BLUE, BarStyle.SEGMENTED_10);
        timeLeft = time * 60;
        totalTime = timeLeft;

        // Adding all players to the bar
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            bar.addPlayer(player);
        }

        // Registering the bar and events
        plugin.getServer().getPluginManager().registerEvents(competitionListener, plugin);
        updater = plugin.getServer().getScheduler().runTaskTimer(plugin, this::updateTimer, 0, 20L);
    }

    // Stops the boss bar timer
    void removeTimer() {
        updater.cancel();
        bar.setProgress(0);
        HandlerList.unregisterAll(competitionListener);
        bar.removeAll();
        plugin.getServer().removeBossBar(key);
        bar = null;
    }

    // Method to update the health bar and title
    private void updateTimer() {
        timeLeft--;

        if (timeLeft < 0) {
            return;
        }

        bar.setTitle(ChatColor.translateAlternateColorCodes('&', config.getTimebarTitle().replace("%time%", getTime())));
        bar.setProgress(timeLeft.doubleValue() / totalTime);
    }

    // Returns a formatted string of time left
    private String getTime() {
        String time = "";
        int minutes = (int)(timeLeft / 60);
        int seconds = (int)(timeLeft % 60);

        if (minutes > 0) {
            time = minutes + "m ";
        }

        time += seconds + "s";

        return time;
    }
}
