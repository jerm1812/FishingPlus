package me.baryonyx.fishingplus.shop;

import me.baryonyx.fishingplus.FishingPlus;
import me.baryonyx.fishingplus.fishing.Fish;
import me.baryonyx.fishingplus.fishing.FishingMap;
import me.baryonyx.fishingplus.fishing.Reward;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;


public class RewardConverter {
    private FishingMap fishingMap;
    private NamespacedKey fishLength;
    private NamespacedKey rewardModifier;
    private NamespacedKey rewardType;

    public RewardConverter(FishingMap fishingMap, NamespacedKey fishLength, NamespacedKey rewardModifier, NamespacedKey fishType) {
        this.fishingMap = fishingMap;
        this.fishLength = new NamespacedKey(FishingPlus.getPlugin(), "fishLength");
        this.rewardModifier = new NamespacedKey(FishingPlus.getPlugin(), "rewardModifier");
        this.rewardType = new NamespacedKey(FishingPlus.getPlugin(), "rewardType");
    }

    public Reward convertItemToReward(ItemMeta itemMeta) {
        PersistentDataContainer data = itemMeta.getPersistentDataContainer();
        String itemName = data.get(rewardType, PersistentDataType.STRING);
        String itemModifier = data.get(rewardModifier, PersistentDataType.STRING);
        double fishLength = 0;
        if ()
        double itemLength = data.get(fishLength, PersistentDataType.DOUBLE);

        return new Fish();
    }

    //TODO
}
