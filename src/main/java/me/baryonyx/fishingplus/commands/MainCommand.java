package me.baryonyx.fishingplus.commands;

import com.google.inject.Inject;
import me.baryonyx.fishingplus.FishingPlus;
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
            if (args.length == 1)
        }
        else {
            sender.sendMessage("This command can only be issued by a player!");
        }

        return false;
    }

    private boolean helpCommand(CommandSender sender) {
        return true;
    }
}
