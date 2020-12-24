package me.baryonyx.fishingplus.fishing.Competition;

import me.baryonyx.fishingplus.fishing.Fish;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class Entry implements Comparable<Entry> {
    public UUID player;
    public Fish fish;

    Entry(Player player, Fish fish) {
        this.player = player.getUniqueId();
        this.fish = fish;
    }

    // Sorting entries
    @Override
    public int compareTo(@NotNull Entry o) {
        if (this.fish.length >= o.fish.length) {
            return -1;
        }
        else {
            return 0;
        }
    }
}
