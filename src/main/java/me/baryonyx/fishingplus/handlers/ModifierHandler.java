package me.baryonyx.fishingplus.handlers;

import me.baryonyx.fishingplus.configuration.RewardConfiguration;
import me.baryonyx.fishingplus.fishing.Modifier;
import org.jetbrains.annotations.NotNull;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

public class ModifierHandler {
    private final RewardConfiguration config;
    private final Random random = new Random();
    private NavigableMap<Double, Modifier> modifierMap = new TreeMap<>();
    private double totalWeight = 0;

    public ModifierHandler(RewardConfiguration config) {
        this.config = config;
    }

    public void addToMap(@NotNull Modifier modifier) {
        totalWeight += modifier.chance;
        modifierMap.put(totalWeight, modifier);
    }

    public Modifier getRandomModifier() {
        double value = random.nextDouble() * totalWeight;
        return modifierMap.higherEntry(value).getValue();
    }

    public Modifier getModifier(String name) {
        for (Modifier modifier : modifierMap.values()) {
            if (modifier.name.equals(name))
                return modifier;
        }

        return null;
    }
}
