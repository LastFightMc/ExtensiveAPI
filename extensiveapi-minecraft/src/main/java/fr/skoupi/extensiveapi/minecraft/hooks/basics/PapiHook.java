package fr.skoupi.extensiveapi.minecraft.hooks.basics;

import fr.skoupi.extensiveapi.minecraft.hooks.AbstractHook;
import me.clip.placeholderapi.PlaceholderAPI;

public class PapiHook extends AbstractHook<PlaceholderAPI> {


    public PapiHook() {
        super("PlaceholderAPI", "me.clip.placeholderapi.PlaceholderAPI");
    }

    @Override
    public boolean registerHook()
    {
        return pluginEnabled();
    }

}
