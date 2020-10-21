package me.baryonyx.fishingplus.fishing.Competition;

import me.baryonyx.fishingplus.FishingPlus;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class TimerBar {
    private FishingPlus plugin;
    private NamespacedKey key;
    public BossBar bar = null;
    private BukkitTask updater;
    private Long timeLeft;
    private Long totalTime;

    public TimerBar(FishingPlus plugin) {
        this.plugin = plugin;
        key = new NamespacedKey(plugin, "fishingplus-competition-timer");
    }

    void startTimer(Long time) {
        bar = plugin.getServer().createBossBar(key, "", BarColor.BLUE, BarStyle.SEGMENTED_10);
        timeLeft = time * 60;
        totalTime = timeLeft;

        for (Player player : plugin.getServer().getOnlinePlayers()) {
            bar.addPlayer(player);
        }

        updater = plugin.getServer().getScheduler().runTaskTimer(plugin, this::updateTimer, 0, 20L);
    }

    void removeTimer() {
        updater.cancel();
        bar.setTitle("");
        bar.setProgress(0);
        bar.removeAll();
        plugin.getServer().removeBossBar(key);
        bar = null;
    }

    private void updateTimer() {
        timeLeft--;
        bar.setTitle(Announcements.coloredMessage("&b&lFishing Contest &r[" + timeLeft + " left]"));
        bar.setProgress(timeLeft.doubleValue() / totalTime);
    }
}
