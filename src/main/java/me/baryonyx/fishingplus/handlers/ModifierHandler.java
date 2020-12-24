package me.baryonyx.fishingplus.handlers;

import me.baryonyx.fishingplus.fishing.Modifier;
import me.baryonyx.fishingplus.fishing.Reward;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ModifierHandler {
    private final Random random = new Random();
    private NavigableMap<Double, Modifier> modifierMap = new TreeMap<>();
    private double totalWeight = 0;

    // Adds a modifier to the map
    public void addToMap(@NotNull Modifier modifier) {
        totalWeight += modifier.chance;
        modifierMap.put(totalWeight, modifier);
    }

    // Gets a weighted random modifier from the modifiers a fish has
    @Nullable
    public Modifier getRandomPossibleModifier(Reward reward) {
         TreeMap<Double, Modifier> map = new TreeMap<>();
         double possibleWeight = 0;

         for (String name : reward.modifiers) {
             Modifier modifier = getModifier(name);

             if (modifier != null) {
                 possibleWeight += modifier.chance;
                 map.put(possibleWeight, modifier);
             }
         }

         double value = random.nextDouble() * possibleWeight;

         if (possibleWeight == 0) {
             return null;
         }

         return map.higherEntry(value).getValue();
    }

    // Gets a specific modifier from the map
    private Modifier getModifier(String name) {
        for (Modifier modifier : modifierMap.values()) {
            if (modifier.name.equals(name))
                return modifier;
        }

        return null;
    }

    public void clear() {
        totalWeight = 0;
        modifierMap.clear();
    }
}
