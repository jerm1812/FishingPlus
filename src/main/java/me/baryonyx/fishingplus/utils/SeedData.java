package me.baryonyx.fishingplus.utils;

import me.baryonyx.fishingplus.competition.fish.Type;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;


import java.util.Arrays;
import java.util.List;

public class SeedData {

    public static List<Type> seedFishTypes() {
        List<Type> fish = Arrays.asList(
                new Type("Small Fry", 1f, 1f, Material.COD, .5f),
                new Type("Bass", 10f, 3f, Material.COD, 1f),
                new Type("Northern Pike", 15f, 10f, Material.COD, 1.5f),
                new Type("Blue Gill", 10f, 7.5f, Material.COD, 1.25f)
        );

        return fish;
    }
}
