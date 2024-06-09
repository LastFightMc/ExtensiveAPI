package fr.skoupi.extensiveapi.minecraft.hooks;

/*  Hooks
 *  By: vSKAH <vskahhh@gmail.com>

 * Created with IntelliJ IDEA
 * For the project ExtensiveAPI
 */

import fr.skoupi.extensiveapi.minecraft.ExtensiveCore;
import fr.skoupi.extensiveapi.minecraft.hooks.basics.*;
import lombok.Getter;

import java.util.HashMap;

public class Hooks {

    @Getter
    private final HashMap<String, AbstractHook<?>> loaded = new HashMap<>();
    private static Hooks instance;

    public Hooks() {
        instance = this;
        hookDefaultsPlugins();
    }

    /**
     * Hook the default plugins.
     */
    private void hookDefaultsPlugins() {
        hookPlugin(new WorldguardHook());
        hookPlugin(new VaultHook());
        hookPlugin(new LandsHook());
        hookPlugin(new JobsHook());
        hookPlugin(new RosePlayerPointsHook());
        hookPlugin(new RedisEconomyHook());
        hookPlugin(new FactionHook());
    }

    /**
     * If the hook is registered, add it to the pluginHooks map
     *
     * @param hook The hook to register.
     */
    public void hookPlugin(AbstractHook<?> hook) {
        if (hook.registerHook()) {
            ExtensiveCore.getInstance().getLogger().info("Hooked " + hook.getHookName());
            loaded.put(hook.getHookName(), hook);
            loaded.put(hook.getOriginalHookName(), hook);
        }
    }


    /**
     * If the hook is not null, and the plugin is enabled, and the class exists, then return true
     *
     * @param name The name of the plugin you want to check.
     * @return A boolean value.
     */
    public boolean isHooked(String name, boolean useBukkitCheck) {
        if (!useBukkitCheck)
            return loaded.containsKey(name.toUpperCase());

        try {
            return loaded.get(name.toUpperCase()).pluginEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    public static Hooks getInstance() {
        return instance == null ? new Hooks() : instance;
    }

}