package me.baryonyx.fishingplus.fishing;

import me.baryonyx.fishingplus.configuration.Config;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
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
        Reward reward = new Reward("Fry", Material.COD, .75f, 1f);
        Reward reward2 = new Reward("Bass", Material.COD, .25f, 1f);
        Reward reward4 = new Reward("yup", Material.COD, .25f, 1f);
        Reward reward5 = new Reward("test", Material.COD, .25f, 1f);
        Reward reward6 = new Reward("yeah", Material.COD, .25f, 1f);
        addToRewardMap(10d, reward);
        addToRewardMap(15d, reward2);
        addToRewardMap(20d, reward4);
        addToRewardMap(25d, reward5);
        addToRewardMap(30d, reward6);
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
