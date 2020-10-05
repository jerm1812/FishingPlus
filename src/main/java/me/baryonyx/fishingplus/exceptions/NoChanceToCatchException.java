package me.baryonyx.fishingplus.exceptions;

public class NoChanceToCatchException extends Exception {
    private final String rewardName;

    public NoChanceToCatchException(final String rewardName) {
        this.rewardName = rewardName;
    }

    public String getRewardName() {
        return this.rewardName;
    }
}
