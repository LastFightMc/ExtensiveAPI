package fr.skoupi.extensiveapi.minecraft.commands;

/*  CommandLoader
 *  By: vSKAH <vskahhh@gmail.com>

 * Created with IntelliJ IDEA
 * For the project ExtensiveAPI
 */

import co.aikar.commands.BaseCommand;
import co.aikar.commands.PaperCommandManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Getter
public class CommandLoader {

    private final PaperCommandManager paperCommandManager;
    private static final ConcurrentHashMap<String, List<BaseCommand>> commands = new ConcurrentHashMap<>();

    /**
     * Constructor
     * Please don't instantiate this class directly, use the `ExtensiveCore.getCommandLoader()` method instead
     * @param paperCommandManager
     *
     * @implNote We use the PaperCommandManager from Aikar's Command Framework
     * @see <a href="https://github.com/AdvancedCustomFields/acf">Aikar's Command Framework</a>
     */
    public CommandLoader(PaperCommandManager paperCommandManager) {
        this.paperCommandManager = paperCommandManager;
    }

    /**
     * We enable the `help` command, we register a completion for the `players` argument,
     * and we set the default locale to `Locale.FRANCE`
     */
    public void registerDefault() {
        paperCommandManager.enableUnstableAPI("help");
        paperCommandManager.getCommandCompletions().registerAsyncCompletion("players", c -> Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList()));
        paperCommandManager.getLocales().setDefaultLocale(Locale.FRANCE);
    }


    /**
     * Register a command
     *
     * @param plugin  The instance of the plugin.
     * @param command The command to register.
     *
     * @see BaseCommand for the `command` parameter.
     */
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

    /**
     * Register multiple commands at once.
     *
     * @param plugin   The instance of the plugin.
     * @param commands The commands to register.
     * @see <a href="https://www.geeksforgeeks.org/variable-arguments-varargs-in-java/">Variable Arguments (Varargs) in Java</a> for the `commands` parameter.
     */
    public void registerCommands(JavaPlugin plugin, BaseCommand... commands) {
        for (BaseCommand command : commands) {
            registerCommand(plugin, command);
        }
    }

    /**
     * Unregister all commands registered by a "children" plugin
     *
     * @param plugin The instance of the "children" plugin
     */
    public void unregisterCommands(JavaPlugin plugin) {
        unregisterCommands(plugin.getName());
    }

    /**
     * Unregister all commands registered by a "children" plugin
     *
     * @param pluginName The name of the plugin
     */
    public void unregisterCommands(String pluginName) {
        if (!commands.containsKey(pluginName)) return;

        for (BaseCommand command : commands.get(pluginName)) {
            paperCommandManager.unregisterCommand(command);
        }

        commands.remove(pluginName);
    }

    /**
     * Get the registered commands
     * Warning: This method returns a copy of the commands map and not the original map
     * You can't modify the original map using this method
     *
     * @return HashMap<String, List < BaseCommand>> commands. String is the plugin name and List<BaseCommand> is the list of commands registered by the plugin
     */
    public HashMap<String, List<BaseCommand>> getCommands() {
        return new HashMap<>(commands);
    }

}
