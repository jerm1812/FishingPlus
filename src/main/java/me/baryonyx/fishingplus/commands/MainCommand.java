package me.baryonyx.fishingplus.commands;

import me.baryonyx.fishingplus.FishingPlus;
import me.baryonyx.fishingplus.handlers.CatchHandler;
import me.baryonyx.fishingplus.shop.FishingShop;
import me.baryonyx.fishingplus.utils.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;


public class MainCommand implements CommandExecutor {
    private FishingPlus plugin;
    private CatchHandler catchHandler;
    private FishingShop fishingShop;

    public MainCommand(FishingPlus plugin, CatchHandler catchHandler, FishingShop fishingShop) {
        this.plugin = plugin;
        this.catchHandler = catchHandler;
        this.fishingShop = fishingShop;
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

            if (args[0].equals("sellall")) {
                fishingShop.sellRewards((Player)sender);
            }

            if (args[0].equals("shop"))
                return rewardShop((Player)sender);
        }
        else {
            plugin.getLogger().info(Messages.playerOnlyCommand);
        }

        return false;
    }

    private boolean rewardShop(Player sender) {
        Inventory inventory = new Inventory() {
        }

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

    private boolean testMap(@NotNull CommandSender sender) {
        Player target = sender.getServer().getPlayerExact(sender.getName());
        for (int i = 0; i < 100; i++) {
            target.getInventory().addItem(catchHandler.triggerCatchEvent((Player)sender));
        }

        return true;
    }

    //TODO Setup commands
    // - setup permission for the commands
}
