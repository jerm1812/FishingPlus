package me.baryonyx.fishingplus.competition;

import com.google.inject.Inject;
import me.baryonyx.fishingplus.competition.fish.Fish;
import me.baryonyx.fishingplus.configuration.Config;

import java.util.HashMap;
import java.util.Map;

public class FishCodeTable {
    private Config config;
    public Map<String, Fish> fishMap;

    @Inject
    public FishCodeTable(Config config) {
        this.config = config;
        fishMap = new HashMap<>();
    }

    public void loadFishMap() {

    }


}
