package me.baryonyx.fishingplus.commands;

import me.baryonyx.fishingplus.FishingPlus;
import me.baryonyx.fishingplus.utils.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BaseCommand implements CommandExecutor {
    private FishingPlus plugin;
    private CompetitionCommand competitionCommand;
    private ReloadCommand reloadCommand;
    private ShopCommand shopCommand;
    private TestCommand testCommand;
    private HelpCommand helpCommand;

    public BaseCommand(FishingPlus plugin, CompetitionCommand competitionCommand, ReloadCommand reloadCommand, ShopCommand shopCommand, TestCommand testCommand, HelpCommand helpCommand) {
        this.plugin = plugin;

        this.competitionCommand = competitionCommand;
        this.reloadCommand = reloadCommand;
        this.shopCommand = shopCommand;
        this.testCommand = testCommand;
        this.helpCommand = helpCommand;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player)sender;

            // Help command
            if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
                return helpCommand.run(player);
            }

            // Competition command
            if (args[0].equalsIgnoreCase("comp")) {
                return competitionCommand.run(player, args);
            }

            // Testing fish map command
            if (args[0].equalsIgnoreCase("test")) {
                return testCommand.run(player);
            }

            // Sell all rewards in inventory command
            if (args[0].equalsIgnoreCase("sellall")) {
                return shopCommand.sellAll(player);
            }

            // Reload command
            if (args[0].equalsIgnoreCase("reloadConfig")) {
                return reloadCommand.run(player, args);
            }

            // Open shop command
            if (args[0].equalsIgnoreCase("shop")) {
                return shopCommand.run(player);
            }
        }
        else {
            plugin.getLogger().info(Messages.playerOnlyCommand);
            return true;
        }

        return false;
    }
}
