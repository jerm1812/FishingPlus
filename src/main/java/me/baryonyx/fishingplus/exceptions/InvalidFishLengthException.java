package me.baryonyx.fishingplus.exceptions;

public class InvalidFishLengthException extends Throwable {
    private final String itemName;
    private final double length;

    // Should be thrown when fish length is set less than 0
    public InvalidFishLengthException(final String itemName, final double length) {
        this.itemName = itemName;
        this.length = length;
    }

    public String getItemName() {
        return itemName;
    }

    public double getLength() {
        return length;
    }
}
