package me.baryonyx.fishingplus.exceptions;

public class NoFishInModifierException extends Throwable {
    private String modifier;

    public NoFishInModifierException(String modifier) {
        this.modifier = modifier;
    }

    public String getModifier() {
        return modifier;
    }
}
