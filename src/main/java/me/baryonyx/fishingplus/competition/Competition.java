package me.baryonyx.fishingplus.competition;

import com.google.inject.Inject;
import me.baryonyx.fishingplus.FishingPlus;

public class Competition {
    private FishingPlus plugin;
    private FishCodeTable fishCodeTable;

    @Inject
    public Competition(FishingPlus plugin, FishCodeTable fishCodeTable) {
        this.plugin = plugin;
        this.fishCodeTable = fishCodeTable;
    }



}
