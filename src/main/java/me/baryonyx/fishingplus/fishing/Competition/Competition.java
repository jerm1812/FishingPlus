package me.baryonyx.fishingplus.fishing.Competition;

import me.baryonyx.fishingplus.exceptions.InvalidCompetitionStateException;
import me.baryonyx.fishingplus.fishing.Fish;
import org.bukkit.entity.Player;

import java.util.*;

public class Competition {
    private static boolean running = false;
    private Map<UUID, Entry> map = new HashMap<>();

    // Starts a competition and throws if one is already started
    void startCompetition() throws InvalidCompetitionStateException {
        if (running)
            throw new InvalidCompetitionStateException();

        running = true;
    }

    // Stops a competition and throws if one is not running
    void stopCompetition() throws InvalidCompetitionStateException {
        if (!running)
            throw new InvalidCompetitionStateException();

        running = false;
        map.clear();
    }

    // Adds the biggest fish a player catches in a given competition
    void logFish(Player player, Fish fish) {
        if (running) {
            if (!map.containsKey(player.getUniqueId()))
                map.put(player.getUniqueId(), new Entry(player, fish));

            else if (map.get(player.getUniqueId()).fish.length < fish.length)
                map.replace(player.getUniqueId(), new Entry(player, fish));
        }
    }

    // Returns a sorted competition results
    List<Entry> sortCompetitionResults() throws InvalidCompetitionStateException {
        if (!running) {
            throw new InvalidCompetitionStateException();
        }

        List<Entry> entries = new ArrayList<>(map.values());
        Collections.sort(entries);
        return entries;
    }

    // Returns if the competition is running or not
    public static boolean isRunning() {
        return running;
    }

    public void unload() {
        map.clear();
    }
}
