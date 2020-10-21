package me.baryonyx.fishingplus.fishing.shop;

import me.baryonyx.fishingplus.exceptions.RewardNotInMap;
import me.baryonyx.fishingplus.fishing.Reward;
import me.baryonyx.fishingplus.handlers.ItemHandler;
import me.baryonyx.fishingplus.handlers.RewardHandler;
import me.baryonyx.fishingplus.hooks.VaultHook;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class FishingShop {
    private ItemHandler itemHandler;
    private RewardHandler rewardHandler;
    private Economy economy;

    public FishingShop(ItemHandler itemHandler, RewardHandler rewardHandler) {
        this.itemHandler = itemHandler;
        this.rewardHandler = rewardHandler;
        this.economy = VaultHook.getEconomy();
    }

    // Sells all FishingPlus rewards
    public void sellRewards(@NotNull Inventory inventory, @NotNull Player player) {
        Map<ItemStack, Double> rewards = getRewardsInInventory(inventory);
        double total = calculateTotalValue(rewards);

        for (ItemStack item : rewards.keySet()) {
            inventory.remove(item);
        }

        if (total > 0) {
            economy.depositPlayer(player, total);
            player.sendMessage("You sold your rewards for: " + total);
        }
    }

    // Gets all FishingPlus rewards that are in the given inventory
    Map<ItemStack, Double> getRewardsInInventory(@NotNull Inventory inventory) {
        Map<ItemStack, Double> map = new HashMap<>();

        for (ItemStack item : inventory.getContents()) {
            try {
                if (item != null && itemHandler.isReward(item)) {
                    String name = itemHandler.getRewardName(item);
                    Reward reward = rewardHandler.getFishingReward(name);

                    if (reward == null)
                        throw new RewardNotInMap(name);

                    map.put(item, reward.price);
                }
            } catch (RewardNotInMap e) {
                Bukkit.getLogger().warning("Could not find " + e.getName() + " in the reward map");
            }
        }

        return map;
    }

    // Calculates the total value of FishingPlus rewards passed in
    double calculateTotalValue(@NotNull Map<ItemStack, Double> map) {
        double price  = 0;

        for (Map.Entry<ItemStack, Double> entry: map.entrySet()) {
            price += entry.getValue() * entry.getKey().getAmount();
        }

        return price;
    }
}
