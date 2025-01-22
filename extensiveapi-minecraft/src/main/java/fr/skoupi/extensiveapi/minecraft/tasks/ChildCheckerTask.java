/*  ChildCheckerTask
 * By: jimmy "vSKAH" <vskahhh@gmail.com>
 * Created with IntelliJ IDEA
 * For the project ExtensiveAPI
 * 17/10/2024
 */

package fr.skoupi.extensiveapi.minecraft.tasks;

import co.aikar.commands.BaseCommand;
import fr.skoupi.extensiveapi.minecraft.ExtensiveCore;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

/**
 * This task is used to check if the child plugins are still enabled.
 * If the child plugin is disabled, we unregister all commands and listeners.
 *
 * What is a child plugin?
 * A child plugin is a plugin that uses the ExtensiveAPI as a dependency.
 */
public class ChildCheckerTask extends TimerTask {

    @Override
    public void run() {
        PluginManager pm = Bukkit.getPluginManager();
        HashMap<String, List<BaseCommand>> commands = ExtensiveCore.getInstance().getCommandLoader().getCommands();
        for (Map.Entry<String, List<BaseCommand>> commandsEntry : commands.entrySet()) {
            try {
                JavaPlugin plugin = (JavaPlugin) pm.getPlugin(commandsEntry.getKey());
                if (plugin == null || !plugin.isEnabled()) {
                    unregisterMissingChildrenCommands(plugin);
                    unregisterMissingChildrenListeners(plugin);
                }
            } catch (Exception ignored) {}
        }

    }

    private void unregisterMissingChildrenCommands(JavaPlugin plugin) {
        ExtensiveCore.getInstance().getCommandLoader().unregisterCommands(plugin);
    }

    private void unregisterMissingChildrenListeners(JavaPlugin plugin) {
        HandlerList.unregisterAll(plugin);
    }

}
