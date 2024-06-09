package fr.skoupi.extensiveapi.minecraft.hooks.basics;

import com.massivecraft.factions.FactionsPlugin;
import fr.skoupi.extensiveapi.minecraft.hooks.AbstractHook;

public class FactionHook extends AbstractHook<FactionsPlugin> {

    private FactionsPlugin factions;

    public FactionHook() {
        super("Factions", "com.massivecraft.factions.FactionsPlugin");
    }

    @Override
    public boolean registerHook() {
        if(!pluginEnabled()) return false;
        this.factions = FactionsPlugin.getInstance();
        return true;
    }

    @Override
    public FactionsPlugin getHook() {
        return factions;
    }
}
