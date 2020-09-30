package me.baryonyx.fishingplus.commands;

import me.baryonyx.fishingplus.FishingPlus;
import me.baryonyx.fishingplus.fishing.RewardMap;
import me.baryonyx.fishingplus.utils.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MainCommand implements CommandExecutor {
    private FishingPlus plugin;
    private RewardMap rewardMap;

    public MainCommand(FishingPlus plugin, RewardMap rewardMap) {
        this.plugin = plugin;
        this.rewardMap = rewardMap;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            if (args.length == 0)
                return helpCommand(sender);

            if (args[0].equals("competition") || args[0].equals("comp"))
                return competitionCommand(sender, args);

            if (args[0].equals("test")) {
                return testMap(sender);
            }
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

    private boolean testMap(CommandSender sender) {
        Player target = sender.getServer().getPlayerExact(sender.getName());
        for (int i = 0; i < 100; i++) {
            target.getInventory().addItem(rewardMap.createReward(sender));
        }

        return true;
    }
}
