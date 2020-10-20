package me.baryonyx.fishingplus.fishing.Competition;

import me.baryonyx.fishingplus.fishing.Fish;
import me.baryonyx.fishingplus.fishing.Modifier;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class Entry implements Comparable<Entry> {
    public OfflinePlayer player;
    public Fish fish;
    public Modifier modifier;

    Entry(OfflinePlayer player, Fish fish, Modifier modifier) {
        this.player = player;
        this.fish = fish;
        this.modifier = modifier;
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
