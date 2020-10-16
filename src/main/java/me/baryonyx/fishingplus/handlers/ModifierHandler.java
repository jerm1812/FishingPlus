package me.baryonyx.fishingplus.handlers;

import me.baryonyx.fishingplus.fishing.Modifier;
import org.jetbrains.annotations.NotNull;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

public class ModifierHandler {
    private final Random random = new Random();
    private NavigableMap<Double, Modifier> modifierMap = new TreeMap<>();
    private double totalWeight = 0;

    // Adds a modifier to the map
    void addToMap(@NotNull Modifier modifier) {
        totalWeight += modifier.chance;
        modifierMap.put(totalWeight, modifier);
    }

    // Gets a random modifier from the map
    Modifier getRandomModifier() {
        double value = random.nextDouble() * totalWeight;
        return modifierMap.higherEntry(value).getValue();
    }

    // Gets a specific modifier from the map
    public Modifier getModifier(String name) {
        for (Modifier modifier : modifierMap.values()) {
            if (modifier.name.equals(name))
                return modifier;
        }

        return null;
    }
}
