package me.baryonyx.fishingplus.fishing.shop;

import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.trait.Trait;
import org.bukkit.event.EventHandler;

public class CitizensShop extends Trait {

    public CitizensShop() {
        super("fishingshop");
    }

    @EventHandler
    public void onNpcClick(NPCRightClickEvent event) {
        event.getClicker().chat("/fp shop");
    }
}
