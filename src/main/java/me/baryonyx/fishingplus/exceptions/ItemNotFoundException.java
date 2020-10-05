package me.baryonyx.fishingplus.exceptions;

public class ItemNotFoundException extends Exception {
    private final String rewardName;
    private final String itemName;

    public ItemNotFoundException(final String itemName, final String rewardName) {
        this.itemName = itemName;
        this.rewardName = rewardName;
    }

    public String getRewardName() {
        return this.rewardName;
    }

    public String getItemName() {
        return this.itemName;
    }
}
