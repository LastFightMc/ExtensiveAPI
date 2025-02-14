package fr.skoupi.extensiveapi.minecraft.protections;

/*  ProtectionTest
 *  By: vSKAH <vskahhh@gmail.com>

 * Created with IntelliJ IDEA
 * For the project ExtensiveAPI
 */

import com.massivecraft.factions.listeners.FactionsBlockListener;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import fr.skoupi.extensiveapi.minecraft.hooks.Hooks;
import fr.skoupi.extensiveapi.minecraft.utils.MinecraftVersion;
import me.angeschossen.lands.api.integration.LandsIntegration;
import me.angeschossen.lands.api.land.Land;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


public class ProtectionTests {

    public static boolean testPlace(@NotNull Location location, @NotNull Material placedMaterial, @NotNull Player player) {

        if (placedMaterial == Material.BEDROCK && !player.hasPermission("lastfight.placebedrock"))
            return false;

        Hooks hooks = Hooks.getInstance();

        // Checking if the plugin is hooked to WorldGuard.
        if (hooks.isHooked("WORLDGUARD", false)) {
            WorldGuardPlugin worldGuardIntegration = (WorldGuardPlugin) hooks.getLoaded().get("WorldGuard").getHook();
            if (!worldGuardIntegration.createProtectionQuery().testBlockPlace(null, location, placedMaterial))
                return false;
        }

        //Check if the plugin is hooked to Factions
        if (hooks.isHooked("FACTIONS", false)) {
            if (!FactionsBlockListener.playerCanBuildDestroyBlock(player, location, "build", true))
                return false;
        }

        return placedMaterial != Material.AIR;
    }

    /**
     * If the block is not bedrock, liquid, a sign, a banner, or wood, then return the result of the testBreak function
     *
     * @param location The location of the block you want to break.
     * @param player   The player who is breaking the block
     * @return A boolean value.
     */
    public static boolean testBreakHammer(@NotNull Location location, @NotNull Player player) {
        final var block = location.getBlock();
        final var material = block.getType();

        if (material.isBurnable() || material.name().contains("CHEST"))
            return false;

        // It's checking if the server is running on a version of Minecraft that is lower than 1.13.
        // AND It's checking if the block is a spawner
        if ((MinecraftVersion.atLeast(MinecraftVersion.V.v1_13)) && material == Material.SPAWNER) return false;

            // It's checking if the server is running on a version of Minecraft that is at least 1.13.
            // AND It's checking if the block is a spawner
        else if (material == Material.getMaterial("MOB_SPAWNER")) return false;

        return testBreak(location, player);
    }


    /**
     * If the block is not air, liquid, or a chest, and the player is allowed to break it, return true
     *
     * @param location The location of the block that is being broken.
     * @param player   The player who is breaking the block
     * @return A boolean value.
     */
    public static boolean testBreak(@NotNull Location location, @NotNull Player player) {
        final Block block = location.getBlock();

        if (block.getType() == Material.BEDROCK && !player.hasPermission("lastfight.breakbedrock"))
            return false;

        if (block.isLiquid()) return false;

        Hooks hooks = Hooks.getInstance();

        // Checking if the plugin is hooked to Lands.
        if (hooks.isHooked("LANDS", false)) {
            LandsIntegration landsIntegration = (LandsIntegration) hooks.getLoaded().get("Lands").getHook();
            Land land = landsIntegration.getLand(location);
            if (landsIntegration.isClaimed(location) && land != null && !land.getOnlinePlayers().contains(player))
                return false;
        }

        // Checking if the plugin is hooked to WorldGuard.
        if (hooks.isHooked("WORLDGUARD", false)) {
            WorldGuardPlugin worldGuardIntegration = (WorldGuardPlugin) hooks.getLoaded().get("WorldGuard").getHook();
            if (!worldGuardIntegration.createProtectionQuery().testBlockBreak(null, block)) return false;
        }

        if (hooks.isHooked("FACTIONS", false)) {
            if (!FactionsBlockListener.playerCanBuildDestroyBlock(player, location, "destroy", true))
                return false;
        }

        return block.getType() != Material.AIR;
    }


    /**
     * If WorldGuard is loaded, return whether the player can be damaged
     *
     * @param player The player to test.
     * @return A boolean value, true = can be damaged.
     */
    public static boolean testPvp(@NotNull Player player) {
        Hooks hooks = Hooks.getInstance();

        // It's checking if the plugin is hooked to WorldGuard.
        if (hooks.isHooked("WORLDGUARD", false)) {
            WorldGuardPlugin worldGuardIntegration = (WorldGuardPlugin) hooks.getLoaded().get("WorldGuard").getHook();
            return worldGuardIntegration.createProtectionQuery().testEntityDamage(null, player);
        }

        return true;
    }

}
