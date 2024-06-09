package fr.skoupi.extensiveapi.minecraft.hooks.basics;

/*  LandsHook
 *  By: vSKAH <vskahhh@gmail.com>

 * Created with IntelliJ IDEA
 * For the project ExtensiveAPI
 */

import fr.skoupi.extensiveapi.minecraft.ExtensiveCore;
import fr.skoupi.extensiveapi.minecraft.hooks.AbstractHook;
import me.angeschossen.lands.api.integration.LandsIntegration;

/**
 * This class is a hook for the Lands plugin.
 */
public class LandsHook extends AbstractHook<LandsIntegration> {

    private LandsIntegration lands;

    public LandsHook() {
        super("Lands", "me.angeschossen.lands.api.integration.LandsIntegration");
    }

    @Override
    public boolean registerHook() {
        if(!pluginEnabled()) return false;
        this.lands = new LandsIntegration(ExtensiveCore.getInstance());
        return true;
    }

    @Override
    public String getHookName() {
        return "Lands";
    }


    @Override
    public LandsIntegration getHook() {
        return lands;
    }
}
