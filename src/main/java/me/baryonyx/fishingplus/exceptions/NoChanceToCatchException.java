package me.baryonyx.fishingplus.exceptions;

public class NoChanceToCatchException extends Exception {
    private final String rewardName;

    // Should be thrown when the chance to catch a reward is less than or at 0
    public NoChanceToCatchException(final String rewardName) {
        this.rewardName = rewardName;
    }

    public String getRewardName() {
        return this.rewardName;
    }
}
