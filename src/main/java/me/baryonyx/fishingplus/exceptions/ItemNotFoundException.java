package me.baryonyx.fishingplus.exceptions;

public class ItemNotFoundException extends Exception {
    public String itemName;

    public ItemNotFoundException(String itemName) {
        this.itemName = itemName;
    }
}
