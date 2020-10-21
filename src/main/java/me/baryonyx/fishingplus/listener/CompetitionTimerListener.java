package me.baryonyx.fishingplus.listener;

import me.baryonyx.fishingplus.fishing.Competition.TimerBar;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class CompetitionTimerListener implements Listener {
    private TimerBar timerBar;

    public CompetitionTimerListener(TimerBar timerBar) {
        this.timerBar = timerBar;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        timerBar.bar.addPlayer(event.getPlayer());
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        timerBar.bar.removePlayer(event.getPlayer());
    }
}
