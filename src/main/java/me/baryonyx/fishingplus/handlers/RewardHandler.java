package me.baryonyx.fishingplus.handlers;

import me.baryonyx.fishingplus.exceptions.NullItemException;
import me.baryonyx.fishingplus.fishing.Fish;
import me.baryonyx.fishingplus.fishing.Reward;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RewardHandler {

    public static ItemStack convertRewardToItem(Reward reward) {
        return new ItemStack(reward.item, reward.amount);
    }

    public static ItemMeta createRewardMeta(ItemStack item, Reward reward) {
        ItemMeta meta = item.getItemMeta();
        meta.setLore(createRewardLore(reward));
        meta.setDisplayName(reward.displayName);

        return meta;
    }

    public static List<String> createRewardLore(Fish fish) {
        throw new NotImplementedException();
    }

    private static List<String> createRewardLore(Reward reward) {
        if (reward instanceof Fish) {
            Fish fish = (Fish) reward;
            return Arrays.asList(fish.displayName + " length: " + fish.actualLength, "Was caught by " + fish.caughtBy);
        }
        else
            return Collections.singletonList("Was caught by ");
    }
}
