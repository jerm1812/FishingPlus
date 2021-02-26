package me.baryonyx.fishingplus.commands;

import me.baryonyx.fishingplus.configuration.Config;
import me.baryonyx.fishingplus.fishing.RewardLoader;
import me.baryonyx.fishingplus.messaging.Messages;
import me.baryonyx.fishingplus.utils.Permissions;
import org.bukkit.entity.Player;

public class ReloadCommand {
    private Config config;
    private Messages messages;
    private RewardLoader rewardLoader;

    public ReloadCommand(Config config, Messages messages, RewardLoader rewardLoader) {
        this.messages = messages;
        this.rewardLoader = rewardLoader;
        this.config = config;
    }

    // Issuing the command
    boolean run(Player player, String[] args) {
        // Permission check
        if (player.hasPermission(Permissions.reload)) {
            if (args.length == 2) {
                // Player reloads the config
                if (args[1].equalsIgnoreCase("config")) {
                    config.reloadConfig();
                    messages.playerMessage(player, "&7Config has reloaded!");
                    return true;
                }

                else if (args[1].equalsIgnoreCase("messages")) {
                    config.reloadMessages();
                    messages.playerMessage(player, "&7Messages have reloaded!");
                    return true;
                }

                // Player reloadConfig the rewards
                else if (args[1].equalsIgnoreCase("rewards")) {
                    rewardLoader.reload();
                    messages.playerMessage(player, "&7Rewards have reloaded!");
                    return true;
                }
            }

            messages.playerMessage(player, "&7Please enter either config, messages, or rewards to reload!");
        }

        // Failed permission check
        else {
            messages.noPermission(player);
        }

        return true;
    }
}
