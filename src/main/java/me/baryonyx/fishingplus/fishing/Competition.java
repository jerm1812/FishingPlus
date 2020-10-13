package me.baryonyx.fishingplus.fishing;

import me.baryonyx.fishingplus.exceptions.InvalidCompetitionStateException;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class Competition {
    private boolean running = false;
    private Map<Player, Entry> map = new HashMap<>();
    public long startTime;

    public void startCompetition() throws InvalidCompetitionStateException {
        if (running)
            throw new InvalidCompetitionStateException();

        startTime = System.currentTimeMillis() / 1000;
        running = true;
    }

    public void stopCompetition() throws InvalidCompetitionStateException {
        if (!running)
            throw new InvalidCompetitionStateException();

        running = false;
    }

    public void logFish(Player player, Fish fish, Modifier modifier) {
        if (running) {
            if (!map.containsKey(player))
                map.put(player, new Entry(fish, modifier));

            else if (map.get(player).fish.actualLength < fish.actualLength)
                map.replace(player, new Entry(fish, modifier));
        }
    }

    public Map<Player, Entry> getCompetitionStats() {
        return map;
    }

    public boolean isRunning() {
        return running;
    }
}
