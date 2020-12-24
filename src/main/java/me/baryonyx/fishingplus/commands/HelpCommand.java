package me.baryonyx.fishingplus.commands;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HelpCommand {

    // Issuing the command
    boolean run(@NotNull Player player) {
        String message = "[FishingPlus] - The base command for fishing plus is /fp" +
                "\n/fp shop - Will open up the fishing plus shop. Perm: fishingplus.shop" +
                "\n/fp sellall - Sells all fishing plus rewards in the players inventory. Perm: fishingplus.shop.sellall" +
                "\n/fp test - Will give the player a inventory full of rewards to test the reward map. Perm: fishingplus.test" +
                "\n/fp comp - The general competition command. Perm fishingplus.competition" +
                "\n/fp comp start {minutes} - The competition command to start a fishing competition. Perm: fishingplus.competition.start" +
                "\n/fp comp end - The competition command to end a fishing competition. Perm: fishingplus.competition.end";

        player.sendMessage(message);
        return true;
    }
}
