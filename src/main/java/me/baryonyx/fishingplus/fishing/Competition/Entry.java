package me.baryonyx.fishingplus.fishing.Competition;

import me.baryonyx.fishingplus.fishing.Fish;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Entry implements Comparable<Entry> {
    public Player player;
    public Fish fish;

    Entry(Player player, Fish fish) {
        this.player = player;
        this.fish = fish;
    }

    @Override
    public int compareTo(@NotNull Entry o) {
        if (this.fish.actualLength >= o.fish.actualLength) {
            return -1;
        }
        else {
            return 0;
        }
    }
}
