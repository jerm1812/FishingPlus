package me.baryonyx.fishingplus.commands;

import me.baryonyx.fishingplus.utils.Permissions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandCompleter implements TabCompleter {

    // Command completer for the base command
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> commands = new ArrayList<>();
        List<String> completion = new ArrayList<>();

        if (args.length == 1) {
            if (sender.hasPermission(Permissions.startComp) || sender.hasPermission(Permissions.endComp)) {
                commands.add("comp");
            }
            if (sender.hasPermission(Permissions.shop)) {
                commands.add("shop");
            }
            if (sender.hasPermission(Permissions.test)) {
                commands.add("test");
            }
            if (sender.hasPermission(Permissions.reload)) {
                commands.add("reload");
            }
            if (sender.hasPermission(Permissions.sellAll)) {
                commands.add("sellall");
            }

            StringUtil.copyPartialMatches(args[0], commands, completion);
        }
        else if (args.length == 2 && args[0].equalsIgnoreCase("comp")) {
            if (sender.hasPermission(Permissions.top)) {
                commands.add("top");
            }
            if (sender.hasPermission(Permissions.startComp)) {
                commands.add("start");
            }
            if (sender.hasPermission(Permissions.endComp)) {
                commands.add("end");
            }

            StringUtil.copyPartialMatches(args[1], commands, completion);
        }
        else if (args.length == 2 && args[0].equalsIgnoreCase("reload")) {
            if (sender.hasPermission(Permissions.reloadConfig)) {
                commands.add("config");
            }
            if (sender.hasPermission(Permissions.reloadMessages)) {
                commands.add("messages");
            }
            if (sender.hasPermission(Permissions.reloadRewards)) {
                commands.add("rewards");
            }

            StringUtil.copyPartialMatches(args[1], commands, completion);
        }
        else if (args.length == 3 && args[1].equalsIgnoreCase("start")) {
            completion = Arrays.asList("5", "10", "15", "20", "25", "30");
        }

        Collections.sort(completion);
        return completion;
    }
}
