package me.baryonyx.fishingplus.fishing.Competition;

import me.baryonyx.fishingplus.exceptions.InvalidCompetitionStateException;
import me.baryonyx.fishingplus.fishing.Fish;
import me.baryonyx.fishingplus.fishing.Modifier;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class Competition {
    private static boolean running = false;
    private Map<Player, Entry> map = new HashMap<>();

    // Starts a competition and throws if one is already started
    public void startCompetition() throws InvalidCompetitionStateException {
        if (running)
            throw new InvalidCompetitionStateException();

        running = true;
    }

    // Stops a competition and throws if one is not running
    public void stopCompetition() throws InvalidCompetitionStateException {
        if (!running)
            throw new InvalidCompetitionStateException();

        running = false;
        map.clear();
    }

    // Adds the biggest fish a player catches in a given competition
    public void logFish(Player player, Fish fish, Modifier modifier) {
        if (running) {
            if (!map.containsKey(player))
                map.put(player, new Entry(player, fish, modifier));

            else if (map.get(player).fish.actualLength < fish.actualLength)
                map.replace(player, new Entry(player, fish, modifier));
        }
    }

    // Returns the competition stats
    public Map<Player, Entry> getCompetitionStats() {
        return map;
    }

    // Returns if the competition is running or not
    public static boolean isRunning() {
        return running;
    }
}
