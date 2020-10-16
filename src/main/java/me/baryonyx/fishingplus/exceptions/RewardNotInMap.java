package me.baryonyx.fishingplus.exceptions;

public class RewardNotInMap extends Exception {
    private final String name;

    // Should be thrown when a reward name could not be found in a map
    public RewardNotInMap(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
