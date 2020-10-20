package me.baryonyx.fishingplus.fishing.Competition;

import me.baryonyx.fishingplus.configuration.Config;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

public class Announcements {
    private Config config;
    private String prefix;

    public Announcements(Config config) {
        this.config = config;
        this.prefix = config.getBroadcastPrefix();
    }

    // Broadcast for when a competition starts
    public void broadcastCompetitionStart(long time) {
        if (config.isBroadcastStartEnabled()) {
            String message = prefix + "&7A fishing competition has started!";
            if (time != 0)
                message += " The competition will last for " + time + " minutes.";
            Bukkit.broadcastMessage(coloredMessage(message));
        }
    }

    // Broadcast for when a competition ends
    public void broadcastCompetitionEnd() {
        if (config.isBroadcastEndEnabled()) {
            Bukkit.broadcastMessage(coloredMessage(prefix + "&7The fishing competition has ended!"));
        }
    }

    // Broadcast for competition ending warning
    public void broadcastCompetitionTimeLeft(int minutes) {
        Bukkit.broadcastMessage(coloredMessage(prefix + "&7There is " + minutes + " minutes left in the fishing competition!"));
    }

    // Broadcast for player results
    public void broadcastCompetitionResults(String player, String place, String fish, double length) {
        Bukkit.broadcastMessage(coloredMessage(prefix + "&7" + place + " : " + player + ", " + length + " " + fish));
    }

    // Broadcast for when a competition ends without enough participants
    public void broadcastNotEnoughParticipants() {
        Bukkit.broadcastMessage(coloredMessage(prefix + "&7Not enough players joined the competition!"));
    }

    @NotNull
    static String coloredMessage(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

}
