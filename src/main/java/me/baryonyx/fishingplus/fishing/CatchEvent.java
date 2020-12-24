package me.baryonyx.fishingplus.fishing;

import me.baryonyx.fishingplus.handlers.ItemHandler;
import me.baryonyx.fishingplus.handlers.ModifierHandler;
import me.baryonyx.fishingplus.handlers.RewardHandler;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class CatchEvent {
    private RewardHandler rewardHandler;
    private ModifierHandler modifierHandler;
    private ItemHandler itemHandler;

    public CatchEvent(RewardHandler rewardHandler, ModifierHandler modifierHandler, ItemHandler itemHandler) {
        this.rewardHandler = rewardHandler;
        this.modifierHandler = modifierHandler;
        this.itemHandler = itemHandler;
    }

    @Nullable
    public ItemStack handleCatchEvent(Player player) {
        Reward reward = rewardHandler.getRandomFishingReward();
        Modifier modifier = modifierHandler.getRandomPossibleModifier(reward);
        rewardHandler.runCommands(reward, player);
        ItemStack item;

        if (reward instanceof Fish) {
            item = itemHandler.createFishItem(reward.name, player.getName(), rewardHandler.generateWeightedLength((Fish)reward));
        }
        else {
            item = itemHandler.createRewardItem(reward.name, player.getName(), true);
        }

        if (item != null && modifier != null) {
            itemHandler.addModifierToItem(item, modifier.displayName);
        }

        return item;
    }
}
