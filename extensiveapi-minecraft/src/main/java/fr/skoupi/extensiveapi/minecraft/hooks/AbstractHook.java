package fr.skoupi.extensiveapi.minecraft.hooks;

/*  Hook
 *  By: vSKAH <vskahhh@gmail.com>

 * Created with IntelliJ IDEA
 * For the project ExtensiveAPI
 */


import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

public abstract class AbstractHook<R> {

    private final @Getter String originalHookName;
    private final @Getter String hookName;
    private final @Getter String clasz;

    public AbstractHook(String hookName, String clasz) {
        this.originalHookName = hookName;
        this.hookName = hookName.toUpperCase();
        this.clasz = clasz;
    }

    /**
     * Register a hook for the specified event.
     *
     * @return A boolean value.
     */
    public boolean registerHook() {
        return false;
    }

    /**
     * Returns the value of the property.
     *
     * @return The return value is a reference to the object that called the method.
     */
    public R getHook() {
        return null;
    }

    /**
     * * If the class name is null, return false.
     * * If the class name is not null, try to load the class.
     * * If the class is loaded, return true.
     * * If the class is not loaded, return false
     *
     * @return A boolean value.
     */
    public boolean classExists() {
        if (getClasz() == null) return false;
        try {
            Class.forName(getClasz());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    protected <T> T getProvider(Class<T> classz) {
        RegisteredServiceProvider<T> provider = Bukkit.getServer().getServicesManager().getRegistration(classz);
        return provider == null ? null : provider.getProvider();
    }

    /**
     * If the plugin is enabled and the class exists, return true
     *
     * @return The boolean value of whether the plugin is enabled and the class exists.
     */
    public boolean pluginEnabled() {
        Plugin plugin = Bukkit.getPluginManager().getPlugin(originalHookName);
        boolean classExists = classExists();
        boolean pluginEnabled = plugin != null && plugin.isEnabled();
        Bukkit.getLogger().warning("HOOK: " + getHookName() + " class exists: " + classExists + " plugin enabled: " + pluginEnabled);
        return classExists && pluginEnabled;
    }
}