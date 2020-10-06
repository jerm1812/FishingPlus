package me.baryonyx.fishingplus.shop;

import me.baryonyx.fishingplus.FishingPlus;
import me.baryonyx.fishingplus.fishing.Fish;
import me.baryonyx.fishingplus.fishing.FishingMap;
import me.baryonyx.fishingplus.fishing.Modifier;
import me.baryonyx.fishingplus.fishing.Reward;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;


public class RewardConverter {
    private FishingMap fishingMap;
    private NamespacedKey fishLength;
    private NamespacedKey rewardModifier;
    private NamespacedKey rewardType;

    public RewardConverter(FishingMap fishingMap) {
        this.fishingMap = fishingMap;
        this.fishLength = new NamespacedKey(FishingPlus.getPlugin(), "fishLength");
        this.rewardModifier = new NamespacedKey(FishingPlus.getPlugin(), "rewardModifier");
        this.rewardType = new NamespacedKey(FishingPlus.getPlugin(), "rewardType");
    }

    public Reward convertItemToReward(ItemMeta itemMeta) {
        PersistentDataContainer data = itemMeta.getPersistentDataContainer();
        String itemName = data.get(rewardType, PersistentDataType.STRING);
        Reward reward = fishingMap.getReward(itemName);

        if (reward == null)
            return null;

        String itemModifier = data.get(rewardModifier, PersistentDataType.STRING);
        Modifier modifier = fishingMap.getModifier(itemModifier);

        if (modifier != null)
            reward.modifier = modifier;

        if (reward instanceof Fish && data.has(fishLength, PersistentDataType.DOUBLE)) {
            ((Fish) reward).actualLength = data.get(fishLength, PersistentDataType.DOUBLE);
        }

        return reward;
    }

    public boolean isAReward(ItemStack item) {
        return item.getItemMeta().getPersistentDataContainer().has(rewardType, PersistentDataType.STRING);
    }

    //TODO create item reader to get Reward and Modifier from ItemStack
    // - create an item writer for more SOC and efficiency
    //FIXME Change the item converter so that namespaced keys are passed in
}
