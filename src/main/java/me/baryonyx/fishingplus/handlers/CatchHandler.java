package me.baryonyx.fishingplus.handlers;

import me.baryonyx.fishingplus.configuration.Config;
import me.baryonyx.fishingplus.fishing.Fish;
import me.baryonyx.fishingplus.fishing.Modifier;
import me.baryonyx.fishingplus.fishing.Reward;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class CatchHandler {
    private final Config config;
    private ItemHandler itemHandler;
    private RewardHandler rewardHandler;
    private ModifierHandler modifierHandler;

    public CatchHandler(Config config, ItemHandler itemHandler, RewardHandler rewardHandler, ModifierHandler modifierHandler) {
        this.config = config;
        this.itemHandler = itemHandler;
        this.rewardHandler = rewardHandler;
        this.modifierHandler = modifierHandler;
    }

    public void triggerCatchEvent(Player player) {
        Reward reward = rewardHandler.getRandomReward();
        Modifier modifier = modifierHandler.getRandomModifier();
        ItemStack item;

        if (reward instanceof Fish)
            item = itemHandler.createFishItem(reward.name, player.getName(), rewardHandler.generateWeightedLength((Fish)reward));
        else
            item = itemHandler.createRewardItem(reward.name, player.getName());

        if (item == null)
            return;
        
        if (modifier != null) {
            itemHandler.addModidiferToItem(Objects.requireNonNull(item.getItemMeta()), modifier.displayName);
        }
    }
}
