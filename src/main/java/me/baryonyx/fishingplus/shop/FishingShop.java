package me.baryonyx.fishingplus.shop;

import me.baryonyx.fishingplus.configuration.Config;
import me.baryonyx.fishingplus.fishing.Reward;
import me.baryonyx.fishingplus.hooks.VaultHook;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class FishingShop {
    private RewardConverter rewardConverter;
    private Config config;
    private Economy economy;

    public FishingShop(RewardConverter rewardConverter, Config config) {
        this.rewardConverter = rewardConverter;
        this.config = config;
        this.economy = VaultHook.getEconomy();
    }

    public void sellRewards(List<ItemStack> items, Player player) {
        for (ItemStack item : items) {
            if (rewardConverter.isAReward(item)) {
                Reward reward = (rewardConverter.convertItemToReward(item.getItemMeta()));
                double price = itemPrice(reward, item.getAmount());
                economy.depositPlayer(player, price);
                player.getInventory().removeItem(item);
            }
        }
    }

    private double itemPrice(Reward reward, int amount) {
        double total = reward.price;

        if (reward.modifier != null) {
            if (config.getModifierAddition())
                total += reward.modifier.priceModifier * amount;
            else
                total += reward.price * reward.modifier.priceModifier;
        }

        return total;
    }

    //TODO implement a shop
    //FIXME have the namespaced keys separate from this class
    // - Implement error handling in this class
}
