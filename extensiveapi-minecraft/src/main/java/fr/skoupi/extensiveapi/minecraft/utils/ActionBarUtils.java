/*  ActionBarUtils
 * By: jimmy "vSKAH" <vskahhh@gmail.com>
 * Created with IntelliJ IDEA
 * For the project ExtensiveAPI
 * 22/01/2025
 */

package fr.skoupi.extensiveapi.minecraft.utils;

import lombok.experimental.UtilityClass;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class ActionBarUtils {

    public static void sendActionBar(@NotNull Player player, @NotNull String message) {
        sendActionBar((CraftPlayer) player, message);
    }

    public static void sendAsyncActionBar(@NotNull JavaPlugin plugin, @NotNull Player player, @NotNull String message) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> sendActionBar(player, message), 5L);
    }

    public static void sendActionBar(@NotNull CraftPlayer player, @NotNull String message) {
        IChatBaseComponent chatComponent = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + message + "\"}");
        PacketPlayOutChat packet = new PacketPlayOutChat(chatComponent, (byte) 2);
        player.getHandle().playerConnection.sendPacket(packet);
    }

}
