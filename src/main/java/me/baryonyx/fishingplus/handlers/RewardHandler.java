package me.baryonyx.fishingplus.handlers;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import me.baryonyx.fishingplus.FishingPlus;
import me.baryonyx.fishingplus.exceptions.ItemNotFoundException;
import me.baryonyx.fishingplus.exceptions.NullItemException;
import me.baryonyx.fishingplus.fishing.Fish;
import me.baryonyx.fishingplus.fishing.Reward;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RewardHandler {
    private FishingPlus plugin;

    public RewardHandler() {
        this.plugin = FishingPlus.getPlugin();
    }

    public static ItemStack convertRewardToItem(Reward reward) {
        return new ItemStack(reward.item, reward.amount);
    }

    public static ItemMeta createRewardMeta(ItemStack item, Reward reward) {
        ItemMeta meta = item.getItemMeta();
        meta.setLore(createRewardLore(reward));
        meta.setDisplayName(reward.displayName);

        return meta;
    }

    public static List<String> createRewardLore(Fish fish) {
        throw new NotImplementedException();
    }

    private static List<String> createRewardLore(Reward reward) {
        if (reward instanceof Fish) {
            Fish fish = (Fish) reward;
            return Arrays.asList(fish.displayName + " length: " + fish.actualLength, "Was caught by " + fish.caughtBy);
        }
        else
            return Collections.singletonList("Was caught by ");
    }

    @Nullable
    private String getRewardDisplayName(ConfigurationSection section, String name) {
        return section.getString(name + ".display-name");
    }

    @Nullable
    private double getMinFishLength(ConfigurationSection section, String name) {
        return section.getDouble(name + ".minLength");
    }

    @Nullable
    private double getMaxFishLength(ConfigurationSection section, String name) {
        return section.getDouble(name + ".maxLength");
    }

    @NotNull
    private double getRewardChance(ConfigurationSection section, String name) {
        return section.getDouble(name + "chance");
    }

    @NotNull
    private Material getRewardMaterial(ConfigurationSection section, String name) throws ItemNotFoundException {
        Material material = Material.matchMaterial(section.getString(name + ".item.id"));

        if (material == null)
            throw new ItemNotFoundException(name);

        return material;
    }

    @NotNull
    private double getRewardPrice(ConfigurationSection section, String name) {
        return section.getDouble(name + ".price");
    }

    @Nullable
    private List<String> getRewardLore(ConfigurationSection section, String name) {
        return section.getStringList(name + "item.lore");
    }

    public Reward loadNewReward(ConfigurationSection section, String name) {
        try {
            String displayname = getRewardDisplayName(section, name);
            Material material = getRewardMaterial(section, name);
            double chance = getRewardChance(section, name);
            double price = getRewardPrice(section, name);
            double maxLength = getMaxFishLength(section, name);
            double minLength = getMinFishLength(section, name);


            return (maxLength == 0) ? new Reward(name, displayname, material, chance, )

        }
        catch (ItemNotFoundException e) {
            plugin.getLogger().warning("There was an error loading the item materal ");
        }

    }
}
