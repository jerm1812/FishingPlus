package me.baryonyx.fishingplus.handlers;

import me.baryonyx.fishingplus.configuration.Config;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class ChatHandler {
    private Config config;

    public ChatHandler(Config config) {
        this.config = config;
    }

    void broadcastCompetitionStart(long time) {
        if (config.isBroadcastStartEnabled()) {
            String message = "&8[&r&bFishingPlus&8] &6-&r &7A fishing competition has started!";
            if (time != 0)
                message += " The competition will last for " + (time/60/20) + " minutes.";
            Bukkit.broadcastMessage(coloredMessage(message));
        }
    }

    void broadcastCompetitionEnd() {
        if (config.isBroadcastEndEnabled()) {
            Bukkit.broadcastMessage(coloredMessage("&8[&r&bFishingPlus&8] &6-&r &7The fishing competition has ended!"));
        }
    }

    void broadcastCompetitionTimeLeft() {

    }

    private String coloredMessage(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    //TODO implement a chat handler
}
