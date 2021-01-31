package me.baryonyx.fishingplus.messaging;

import me.baryonyx.fishingplus.FishingPlus;
import me.baryonyx.fishingplus.configuration.Config;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Messages {
    private FishingPlus plugin;
    private Config config;
    private String prefix;


    public Messages(FishingPlus plugin, Config config) {
        this.plugin = plugin;
        this.config = config;

        prefix = config.getMessageString("message-prefix");
    }

    // Sends a colored message with the prefix to a player
    public void playerMessage(Player player, String message) {
        player.sendMessage(coloredMessage(prefix + message));
    }

    // Sends a message to all players
    private void broadcast(String message) {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            player.sendMessage(coloredMessage(prefix + message));
        }
    }

    @NotNull
    public static String coloredMessage(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    // Creates and sends out the competition start message
    public void competitionStart(Long time) {
        if (config.getConfigBool("broadcast-competition-start")) {
            String message;

            // If the competition is timed
            if (time != 0) {
                message = config.getMessageString("timed-competition-start").replace("%time%", String.valueOf(time));
            } else {
                message = config.getMessageString("untimed-competition-start");
            }

            broadcast(message);
        }
    }

    public void notEnoughParticipants() {
        broadcast(config.getMessageString("not-enough-participants"));
    }

    public void competitionResult(String standings, int rank, Player player) {
        StringBuilder message = new StringBuilder(config.getMessageString("competition-end") + "\n");
        message.append(standings);

        if (rank == 0) {
            message.append(config.getMessageString("did-not-participate"));
        }
        else if (rank > config.getConfigInt("players-displayed")) {
            message.append(config.getMessageString("competition-rank"));
        }

        playerMessage(player, message.toString());
    }

    // Displays caught fish to player
    public void caughtFish(Player player, String fish, String modifier, double length) {
        String message = config.getMessageString("fish-caught")
                .replace("%fish%", fish)
                .replace("%modifier%", modifier)
                .replace("%length%", String.valueOf(length));

        playerMessage(player, message);
    }

    // Displays the total price of rewards sold to player
    public void sellRewards(Player player, double total) {
        String message = config.getConfigString("sell-message").replace("%total%", String.valueOf(total));
        playerMessage(player, message);
    }

    // Returns the ordinal of a number
    public static String ordinal(int i)  {
        String[] suffixes = new String[] { "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th" };
        switch (i % 100) {
            case 11:
            case 12:
            case 13:
                return i + "th";
            default:
                return i + suffixes[i % 10];

        }
    }

    public void competitionStandings(Player player, String standings) {
        playerMessage(player, config.getMessageString("competition-standings") + "\n" + standings);
    }

    public void noCompetitionRunning(Player player) {
        playerMessage(player, config.getMessageString("no-competition"));
    }

    public void shopDisabled(Player player) {
        playerMessage(player, config.getMessageString("shop-disabled"));
    }

    public void noPermission(Player player) {
        playerMessage(player, config.getMessageString("no-permission"));
    }
}
