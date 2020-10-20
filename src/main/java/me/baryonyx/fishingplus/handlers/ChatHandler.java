package me.baryonyx.fishingplus.handlers;

import me.baryonyx.fishingplus.configuration.Config;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

public class ChatHandler {
    private Config config;
    private String prefix;

    public ChatHandler(Config config) {
        this.config = config;
        this.prefix = config.getBroadcastPrefix();
    }

    void broadcastCompetitionStart(long time) {
        if (config.isBroadcastStartEnabled()) {
            String message = prefix + "&7A fishing competition has started!";
            if (time != 0)
                message += " The competition will last for " + time + " minutes.";
            Bukkit.broadcastMessage(coloredMessage(message));
        }
    }

    void broadcastCompetitionEnd() {
        if (config.isBroadcastEndEnabled()) {
            Bukkit.broadcastMessage(coloredMessage(prefix + "&7The fishing competition has ended!"));
        }
    }

    void broadcastCompetitionTimeLeft(int minutes) {
        Bukkit.broadcastMessage(coloredMessage(prefix + "&7There is " + minutes + " minutes left in the fishing competition!"));
    }

    void broadcastCompetitionResults(String player, String place, String fish, double length) {
        Bukkit.broadcastMessage(coloredMessage(prefix + "&7" + place + " : " + player + ", " + length + " " + fish));
    }

    @NotNull
    static String coloredMessage(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

}
