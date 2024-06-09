package fr.skoupi.extensiveapi.minecraft.hooks.basics;

/*  WorldguardHook
 *  By: vSKAH <vskahhh@gmail.com>

 * Created with IntelliJ IDEA
 * For the project ExtensiveAPI
 */

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import fr.skoupi.extensiveapi.minecraft.hooks.AbstractHook;

/**
 * This class is a hook for WorldGuard.
 */
public class WorldguardHook extends AbstractHook<WorldGuardPlugin> {

    public WorldguardHook() {
        super("WorldGuard", "com.sk89q.worldguard.bukkit.WorldGuardPlugin");
    }

    @Override
    public boolean registerHook() {
        return pluginEnabled();
    }


    @Override
    public WorldGuardPlugin getHook() {
        return WorldGuardPlugin.inst();
    }
}
