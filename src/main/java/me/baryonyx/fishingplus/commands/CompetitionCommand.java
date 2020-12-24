package me.baryonyx.fishingplus.commands;

import me.baryonyx.fishingplus.fishing.Competition.Runner;
import me.baryonyx.fishingplus.messaging.Messages;
import me.baryonyx.fishingplus.utils.Permissions;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CompetitionCommand {
    private Runner runner;
    private Messages messages;

    public CompetitionCommand(Runner runner, Messages messages) {
        this.runner = runner;
        this.messages = messages;
    }

    // Issuing the command
    boolean run(@NotNull Player player, @NotNull String[] args) {
        // Displays the current competition standings
        if (args.length == 2 && args[1].equalsIgnoreCase("top")) {
            if (!player.hasPermission(Permissions.top)) {
                messages.noPermission(player);
                return true;
            }

            String standings = runner.getCompetitionStandings(null);

            if (standings == null) {
                messages.noCompetitionRunning(player);
            } else {
                messages.competitionStandings(player, standings);
            }
        }

        // Starting a un-timed competition
        else if (args.length == 2 && args[1].equalsIgnoreCase("start")) {
            if (player.hasPermission(Permissions.startComp) && !player.hasPermission(Permissions.startUntimedComp)) {
                messages.playerMessage(player, "&7Please put in a number for the minutes!");
                return true;
            }

            else if (!player.hasPermission(Permissions.startUntimedComp)) {
                messages.noPermission(player);
                return true;
            }

            runner.startUndefinedCompetition();
        }

        // Ending a competition
        else if (args.length == 2 && args[1].equalsIgnoreCase("end")) {
            if (!player.hasPermission(Permissions.endComp)) {
                messages.noPermission(player);
                return true;
            }

            runner.stopCompetition();
        }

        // Starting a timed competition
        else if (args.length == 3 && args[1].equalsIgnoreCase("start")) {
            if (!player.hasPermission(Permissions.startComp)) {
                messages.noPermission(player);
                return true;
            }

            try {
                runner.startTimedCompetition(Long.parseLong(args[2]));
            } catch (NumberFormatException e) {
                messages.playerMessage(player, "&7Please put in a number for the minutes!");
            }
        }

        return true;
    }
}
