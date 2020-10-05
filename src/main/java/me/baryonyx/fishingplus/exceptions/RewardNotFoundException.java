package me.baryonyx.fishingplus.exceptions;

public class RewardNotFoundException extends Exception {
    private final String player;

    public RewardNotFoundException(final String player) {
        this.player = player;
    }

    public String getPlayer() {
        return player;
    }
}
