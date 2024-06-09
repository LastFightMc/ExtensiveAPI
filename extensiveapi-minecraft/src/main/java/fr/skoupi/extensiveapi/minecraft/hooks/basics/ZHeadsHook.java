package fr.skoupi.extensiveapi.minecraft.hooks.basics;

import fr.maxlego08.head.ZHeadManager;
import fr.skoupi.extensiveapi.minecraft.hooks.AbstractHook;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

public class ZHeadsHook extends AbstractHook<ZHeadManager> {

    private ZHeadManager zHeadManager;

    public ZHeadsHook() {
        super("zHead", "fr.maxlego08.head.ZHeadManager");
    }

    @Override
    public boolean registerHook() {
        if (!pluginEnabled()) return false;
        this.zHeadManager = getProvider(ZHeadManager.class);
        return this.zHeadManager != null;
    }


    @Override
    public ZHeadManager getHook() {
        return zHeadManager;
    }
}
