package me.baryonyx.fishingplus.binding;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import me.baryonyx.fishingplus.FishingPlus;

public class PluginBinder extends AbstractModule {

    private final FishingPlus plugin;

    public PluginBinder(FishingPlus plugin) {
        this.plugin = plugin;
    }

    public Injector createInjector() {
        return Guice.createInjector(this);
    }

    @Override
    protected void configure() {
        this.bind(FishingPlus.class).toInstance(this.plugin);
    }
}
