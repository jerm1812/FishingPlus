package me.baryonyx.fishingplus.shop;

import me.baryonyx.fishingplus.configuration.Config;
import me.baryonyx.fishingplus.fishing.Reward;
import me.baryonyx.fishingplus.handlers.ItemHandler;
import me.baryonyx.fishingplus.handlers.RewardHandler;
import me.baryonyx.fishingplus.hooks.VaultHook;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class FishingShop {
    private ItemHandler itemHandler;
    private RewardHandler rewardHandler;
    private Config config;
    private Economy economy;

    public FishingShop(ItemHandler itemHandler, RewardHandler rewardHandler, Config config) {
        this.itemHandler = itemHandler;
        this.rewardHandler = rewardHandler;
        this.config = config;
        this.economy = VaultHook.getEconomy();
    }

    public void sellRewards(Player player) {
        Map<ItemStack, Double> map = getRewardsInInventory(player.getInventory());
        double total = calculateTotalPrice(map);

        for (ItemStack item : map.keySet()) {
            player.getInventory().remove(item);
        }

        economy.depositPlayer(player, total);
        player.sendMessage("You sold your rewards for: " + total);
    }

    private Map<ItemStack, Double> getRewardsInInventory(Inventory inventory) {
        Map<ItemStack, Double> map = new HashMap<>();

        for (ItemStack item : inventory.getContents()) {
            if (item != null && itemHandler.isReward(item)) {
                String name = itemHandler.getRewardName(item);
                Reward reward = rewardHandler.getReward(name);
                map.put(item, reward.price);
            }
        }

        return map;
    }

    private double calculateTotalPrice(Map<ItemStack, Double> map) {
        double price  = 0;

        for (Map.Entry<ItemStack, Double> entry: map.entrySet()) {
            price += entry.getValue() * entry.getKey().getAmount();
        }

        return price;
    }

    //TODO implement a shop
    //FIXME have the namespaced keys separate from this class
    // - Implement error handling in this class
}
