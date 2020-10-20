package me.baryonyx.fishingplus.commands;

import me.baryonyx.fishingplus.FishingPlus;
import me.baryonyx.fishingplus.handlers.CatchHandler;
import me.baryonyx.fishingplus.fishing.Competition.Runner;
import me.baryonyx.fishingplus.hooks.VaultHook;
import me.baryonyx.fishingplus.shop.FishingShop;
import me.baryonyx.fishingplus.shop.FishingShopGui;
import me.baryonyx.fishingplus.utils.Messages;
import me.baryonyx.fishingplus.utils.Permissions;
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
    private Runner runner;

    public MainCommand(FishingPlus plugin, CatchHandler catchHandler, @Nullable FishingShop fishingShop, @Nullable FishingShopGui fishingShopGui, Runner runner) {
        this.plugin = plugin;
        this.catchHandler = catchHandler;
        this.fishingShop = fishingShop;
        this.fishingShopGui = fishingShopGui;
        this.runner = runner;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player)sender;
            if (args.length == 0)
                return helpCommand(player);

            if (args[0].equals("competition") || args[0].equals("comp"))
                return competitionCommand(player, args);

            if (args[0].equals("test")) {
                return testMap(player);
            }

            if (args[0].equals("sellall")) {
                return sellCommand(player);
            }

            if (args[0].equals("shop"))
                return shopCommand((Player)sender);
        }
        else {
            plugin.getLogger().info(Messages.playerOnlyCommand);
        }

        return false;
    }

    private boolean sellCommand(Player player) {
        fishingShop.sellRewards(player.getInventory(), player);
        return true;
    }

    private boolean shopCommand(Player player) {
        if (!player.hasPermission(Permissions.shopAccess)) {
            player.sendMessage("You do not have permission to access the FishingPlus shop!");
            return true;
        }

        if (!VaultHook.isHooked) {
            player.sendMessage(Messages.shopDisabled);
            return true;
        }

        Inventory inventory = fishingShopGui.setupInventory(player);
        fishingShopGui.inventories.put(player, inventory);
        player.openInventory(inventory);
        return true;
    }

    private boolean helpCommand(@NotNull Player player) {
        //FIXME create a help message
        player.sendMessage(Messages.helpCommand);
        return true;
    }

    private boolean competitionCommand(Player player, @NotNull String[] args) {
        if (args.length == 2 && args[1].toLowerCase().equals("start")) {
            if (!player.hasPermission(Permissions.mod) || !player.hasPermission(Permissions.startUntimedComp)) {
                player.sendMessage("You do not have permission to start an un-timed FishingPlus competition!");
                return true;
            }

            runner.startUndefinedCompetition();
            return true;
        }
        else if (args.length == 2 && args[1].toLowerCase().equals("stop")) {
            if (!player.hasPermission(Permissions.mod) || !player.hasPermission(Permissions.endComp)) {
                player.sendMessage("You do not have permission to end a FishingPlus competition!");
                return true;
            }

            runner.stopCompetition();
            return true;
        }
        else if (args.length == 3 && args[1].toLowerCase().equals("start")) {
            if (!player.hasPermission(Permissions.mod) || !player.hasPermission(Permissions.startComp)) {
                player.sendMessage("You do not have permission to start a FishingPlus competition!");
                return true;
            }

            try {
                runner.startTimedCompetition(Long.parseLong(args[2]));
                return true;
            } catch (NumberFormatException e) {
                player.sendMessage("/FishingPlus comp [start|stop] {minutes}");
            }
        }

        return true;
    }

    private boolean testMap(@NotNull Player player) {
        for (int i = 0; i < 100; i++) {
            player.getInventory().addItem(catchHandler.handleCatchEvent(player));
        }

        return true;
    }

    //TODO create commands to add and remove fish
}
