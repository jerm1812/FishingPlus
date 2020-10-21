package me.baryonyx.fishingplus.fishing.Competition;

import me.baryonyx.fishingplus.FishingPlus;
import me.baryonyx.fishingplus.listener.CompetitionTimerListener;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.scheduler.BukkitTask;

public class TimerBar {
    private FishingPlus plugin;
    private NamespacedKey key;
    public BossBar bar = null;
    private BukkitTask updater;
    private CompetitionTimerListener competitionTimerListener = new CompetitionTimerListener(this);
    private Long timeLeft;
    private Long totalTime;

    public TimerBar(FishingPlus plugin) {
        this.plugin = plugin;
        key = new NamespacedKey(plugin, "fishingplus-competition-timer");
    }

    // Starts a boss bar timer
    void startTimer(Long time) {
        bar = plugin.getServer().createBossBar(key, "", BarColor.BLUE, BarStyle.SEGMENTED_10);
        timeLeft = time * 60;
        totalTime = timeLeft;

        for (Player player : plugin.getServer().getOnlinePlayers()) {
            bar.addPlayer(player);
        }

        updater = plugin.getServer().getScheduler().runTaskTimer(plugin, this::updateTimer, 0, 20L);
        plugin.getServer().getPluginManager().registerEvents(competitionTimerListener, plugin);
    }

    // Stops the boss bar timer
    void removeTimer() {
        updater.cancel();
        HandlerList.unregisterAll(competitionTimerListener);
        bar.removeAll();
        plugin.getServer().removeBossBar(key);
        bar = null;
    }

    // Method to update the health bar and title
    private void updateTimer() {
        timeLeft--;
        bar.setTitle(Announcements.coloredMessage("&b&lFishing Contest &r[" + getTime() + "left]"));
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

        time += seconds + "s ";

        return time;
    }
}
