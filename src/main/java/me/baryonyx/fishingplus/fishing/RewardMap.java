package me.baryonyx.fishingplus.fishing;

import me.baryonyx.fishingplus.configuration.Config;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

import static java.lang.Math.random;

public class RewardMap {
    private Config config;
    private final NavigableMap<Double, Reward> map = new TreeMap<>();
    private final Random random;
    private double totalWeight = 0;

    public RewardMap(Config config) {
        this.config = config;
        this.random = new Random();
        loadRewardMap();
    }

    private void loadRewardMap() {
        ConfigurationSection section = config.getRewards();
        for (String name: section.getKeys(false)) {
            String displayName = section.getString(name + ".display-name");
            Material item = Material.matchMaterial(section.getString(name + ".item.id"));
            double chance = section.getDouble(name + ".chance");
            double price = section.getDouble(name + ".display-name");

            Reward reward = new Reward(name, displayName, item, chance, price);
            addToRewardMap(reward.chance, reward);
        }
    }

    private void addToRewardMap(double weight, Reward reward) {
        if (weight > 0) {
            totalWeight += weight;
            map.put(totalWeight, reward);
        }
    }

    private Reward getRandomReward() {
        double value = random.nextDouble() * totalWeight;
        return map.higherEntry(value).getValue();
    }

    public ItemStack createReward(CommandSender sender) {
        Reward reward = getRandomReward();
        ItemStack item = new ItemStack(reward.item);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(reward.displayName);
        meta.setLore(Collections.singletonList(sender.getName() + " caught this fish!"));
        item.setItemMeta(meta);
        return item;
    }

    private float generateWeightedLength(Fish fish) {
        return Math.round((fish.minLength + (fish.maxLength + 1 - fish.minLength) * Math.pow(random(), 1.35)) * 100) / 100;
    }
}
