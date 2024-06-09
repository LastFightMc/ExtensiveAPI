package fr.skoupi.extensiveapi.minecraft.hooks.basics;

import fr.skoupi.extensiveapi.minecraft.hooks.AbstractHook;
import me.arcaniax.hdb.api.HeadDatabaseAPI;

public class HDBHook extends AbstractHook<HeadDatabaseAPI> {

    private HeadDatabaseAPI headDatabaseAPI;

    public HDBHook() {
        super("HeadDatabase", "me.arcaniax.hdb.api.HeadDatabaseAPI");
    }

    @Override
    public boolean registerHook() {
        if (!pluginEnabled()) return false;
        this.headDatabaseAPI = new HeadDatabaseAPI();
        return true;
    }

    @Override
    public HeadDatabaseAPI getHook() {
        return headDatabaseAPI;
    }
}
