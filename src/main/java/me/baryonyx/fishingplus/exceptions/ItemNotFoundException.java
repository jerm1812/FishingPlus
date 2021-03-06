package me.baryonyx.fishingplus.exceptions;

public class ItemNotFoundException extends Exception {
    private final String rewardName;
    private String itemName;

    // Should be thrown when an item cannot be created with the id in the reward list
    public ItemNotFoundException(final String itemName, final String rewardName) {
        this.itemName = itemName;
        this.rewardName = rewardName;
    }

    public ItemNotFoundException(final String rewardName) {
        this.rewardName = rewardName;
    }

    public String getRewardName() {
        return this.rewardName;
    }

    public String getItemName() {
        return this.itemName;
    }
}
