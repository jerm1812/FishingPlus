package me.baryonyx.fishingplus.commands;

import com.google.inject.Inject;
import me.baryonyx.fishingplus.FishingPlus;
import me.baryonyx.fishingplus.utils.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MainCommand implements CommandExecutor {
    @Inject private FishingPlus plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            if (args.length == 0)
                return helpCommand(sender);

            if (args[0].equals("competition") || args[0].equals("comp"))
                return competitionCommand(sender, args);
        }
        else {
            sender.sendMessage(Messages.playerOnlyCommand);
        }

        return false;
    }

    private boolean helpCommand(CommandSender sender) {
        sender.sendMessage(Messages.helpCommand);
        return true;
    }

    private boolean competitionCommand(CommandSender sender, String[] args) {
        if (args.length == 1)
            sender.sendMessage("Starting a competition");
        else
            sender.sendMessage("Starting competition that will last " + args[1] + " seconds");

        return true;
    }
}
