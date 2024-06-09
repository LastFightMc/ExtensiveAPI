package fr.skoupi.extensiveapi.minecraft.commands;

/*  CommandLoader
 *  By: vSKAH <vskahhh@gmail.com>

 * Created with IntelliJ IDEA
 * For the project ExtensiveAPI
 */

import co.aikar.commands.BaseCommand;
import co.aikar.commands.PaperCommandManager;
import fr.skoupi.extensiveapi.minecraft.ExtensiveCore;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public record CommandLoader(@Getter PaperCommandManager paperCommandManager) {

    private static final ConcurrentHashMap<String, List<BaseCommand>> commands = new ConcurrentHashMap<>();

    /**
     * We enable the `help` command, we register a completion for the `players` argument, we register a completion for the
     * `modules` argument, we register the `ModuleCommand` command, and we set the default locale to `Locale.FRANCE`
     */
    public void registerDefault() {
        paperCommandManager.enableUnstableAPI("help");
        paperCommandManager.getCommandCompletions().registerAsyncCompletion("players", c -> Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList()));
        //paperCommandManager.getCommandCompletions().registerAsyncCompletion("modules", handler -> ModuleManager.getModules().keySet());
        //paperCommandManager.registerCommand(new ModuleCommand());
        paperCommandManager.getLocales().setDefaultLocale(Locale.FRANCE);
    }

    public void registerCommand(JavaPlugin plugin, BaseCommand command) {

        if (commands.containsKey(plugin.getName())) {
            commands.get(plugin.getName()).add(command);
            paperCommandManager.registerCommand(command);
            return;
        }

        List<BaseCommand> commands = new ArrayList<>();
        commands.add(command);

        CommandLoader.commands.put(plugin.getName(), commands);
        paperCommandManager.registerCommand(command);
    }

    public void registerCommands(JavaPlugin plugin, BaseCommand... commands) {
        for (BaseCommand command : commands) {
            registerCommand(plugin, command);
        }
    }

    public void unregisterCommands(JavaPlugin plugin) {
        unregisterCommands(plugin.getName());
    }

    public void unregisterCommands(String pluginName) {
        if (!commands.containsKey(pluginName)) return;

        for (BaseCommand command : commands.get(pluginName)) {
            paperCommandManager.unregisterCommand(command);
        }

        commands.remove(pluginName);
    }


    public static class unregisterCommandTask extends TimerTask {

        @Override
        public void run() {
            try {
                PluginManager pm = Bukkit.getPluginManager();
                for (Map.Entry<String, List<BaseCommand>> commandsEntry : commands.entrySet()) {
                    Plugin plugin = pm.getPlugin(commandsEntry.getKey());
                    if (plugin == null || !plugin.isEnabled())
                        ExtensiveCore.getInstance().getCommandLoader().unregisterCommands(commandsEntry.getKey());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}
