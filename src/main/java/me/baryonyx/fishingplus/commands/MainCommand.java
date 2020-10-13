package me.baryonyx.fishingplus.commands;

import me.baryonyx.fishingplus.FishingPlus;
import me.baryonyx.fishingplus.handlers.CatchHandler;
import me.baryonyx.fishingplus.handlers.CompetitionHandler;
import me.baryonyx.fishingplus.hooks.VaultHook;
import me.baryonyx.fishingplus.shop.FishingShop;
import me.baryonyx.fishingplus.shop.FishingShopGui;
import me.baryonyx.fishingplus.utils.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MainCommand implements CommandExecutor {
    private FishingPlus plugin;
    private CatchHandler catchHandler;
    private FishingShop fishingShop;
    private FishingShopGui fishingShopGui;
    private CompetitionHandler competitionHandler;

    public MainCommand(FishingPlus plugin, CatchHandler catchHandler, @Nullable FishingShop fishingShop, @Nullable FishingShopGui fishingShopGui, CompetitionHandler competitionHandler) {
        this.plugin = plugin;
        this.catchHandler = catchHandler;
        this.fishingShop = fishingShop;
        this.fishingShopGui = fishingShopGui;
        this.competitionHandler = competitionHandler;
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
                fishingShop.sellRewards(((Player) sender).getInventory(), (Player)sender);
            }

            if (args[0].equals("shop"))
                return rewardShop((Player)sender);

            if (args[0].equals("start")) {
                return startCompetition(args);
            }
        }
        else {
            plugin.getLogger().info(Messages.playerOnlyCommand);
        }

        return false;
    }

    private boolean startCompetition(@NotNull String[] args) {
        if (args.length == 2) {

        }
        else if (args.length == 1) {
            competitionHandler.startUndefinedCompetition();
            return true;
        }

        return false;
    }

    private boolean rewardShop(Player player) {
        if (!VaultHook.isHooked) {
            player.sendMessage(Messages.shopDisabled);
            return true;
        }

        Inventory inventory = fishingShopGui.setupInventory(player);
        fishingShopGui.inventories.put(player, inventory);
        player.openInventory(inventory);
        return true;
    }

    private boolean helpCommand(@NotNull CommandSender sender) {
        sender.sendMessage(Messages.helpCommand);
        return true;
    }

    private boolean competitionCommand(CommandSender sender, @NotNull String[] args) {
        if (args.length == 2 && args[1].toLowerCase().equals("start")) {
            competitionHandler.startUndefinedCompetition();
            return true;
        }
        else if (args.length == 2 && args[1].toLowerCase().equals("stop")) {
            competitionHandler.startTimedCompetition(Long.parseLong(args[1]));
            return true;
        }
        else if (args.length == 3 && args[1].toLowerCase().equals("start")) {
            try {
                competitionHandler.startTimedCompetition(Long.parseLong(args[1]));
                return true;
            } catch (NumberFormatException e) {
                sender.sendMessage("You tried to enter a time that was not a whole number!");
            }
        }

        sender.sendMessage("/fishingplus comp {start|stop} {minutes}");
        return true;
    }

    private boolean testMap(@NotNull CommandSender sender) {
        Player target = sender.getServer().getPlayerExact(sender.getName());
        for (int i = 0; i < 100; i++) {
            target.getInventory().addItem(catchHandler.handleCatchEvent((Player)sender));
        }

        return true;
    }

    //TODO Setup commands
    // - setup permission for the commands
}
