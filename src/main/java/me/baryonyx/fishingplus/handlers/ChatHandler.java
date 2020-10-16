package me.baryonyx.fishingplus.handlers;

import me.baryonyx.fishingplus.configuration.Config;
import me.baryonyx.fishingplus.fishing.Entry;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class ChatHandler {
    private Config config;

    public ChatHandler(Config config) {
        this.config = config;
    }

    void broadcastCompetitionStart(long time) {
        if (config.isBroadcastStartEnabled()) {
            String message = "&8[&r&bFishingPlus&8] &6-&r &7A fishing competition has started!";
            if (time != 0)
                message += " The competition will last for " + time + " minutes.";
            Bukkit.broadcastMessage(coloredMessage(message));
        }
    }

    void broadcastCompetitionEnd() {
        if (config.isBroadcastEndEnabled()) {
            Bukkit.broadcastMessage(coloredMessage("&8[&r&bFishingPlus&8] &6-&r &7The fishing competition has ended!"));
        }
    }

    void broadcastCompetitionTimeLeft(int minutes) {
        Bukkit.broadcastMessage(coloredMessage("&8[&r&bFishingPlus&8] &6-&r &7There is " + minutes + " minutes left in the fishing competition!"));
    }

    void broadcastCompetitionResults(Map<Player, Entry> results) {

    }

    @NotNull
    static String coloredMessage(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

}
