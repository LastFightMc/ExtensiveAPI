package fr.skoupi.extensiveapi.minecraft.maths;

/*  Cuboid
 *  By: vSKAH <vskahhh@gmail.com>

 * Created with IntelliJ IDEA
 * For the project ExtensiveAPI
 */

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.List;

@Getter
@ToString
public class Cuboid implements Serializable {

    private String worldName;

    private int x1, y1, z1;
    private int x2, y2, z2;

    private World world;

    public Cuboid() {
    }

    //Cuboid from raw values
    private Cuboid(@NotNull String string, int n, int n2, int n3, int n4, int n5, int n6) {
        this.worldName = string;
        this.x1 = Math.min(n, n4);
        this.x2 = Math.max(n, n4);
        this.y1 = Math.min(n2, n5);
        this.y2 = Math.max(n2, n5);
        this.z1 = Math.min(n3, n6);
        this.z2 = Math.max(n3, n6);
    }

    //Cuboid from cuboid
    public Cuboid(@NotNull Cuboid cuboid) {
        this(cuboid.getWorldName(), cuboid.getX1(), cuboid.getY1(), cuboid.getZ1(), cuboid.getX2(), cuboid.getY2(), cuboid.getZ2());
    }

    // It's creating a cuboid from two locations.
    public Cuboid(@NotNull Location location1, @NotNull Location location2) {
        this.worldName = location1.getWorld().getName();
        this.x1 = Math.min(location1.getBlockX(), location2.getBlockX());
        this.y1 = Math.min(location1.getBlockY(), location2.getBlockY());
        this.z1 = Math.min(location1.getBlockZ(), location2.getBlockZ());
        this.x2 = Math.max(location1.getBlockX(), location2.getBlockX());
        this.y2 = Math.max(location1.getBlockY(), location2.getBlockY());
        this.z2 = Math.max(location1.getBlockZ(), location2.getBlockZ());
    }


    /**
     * If the x, y, and z coordinates are within the bounds of the box, return true, otherwise return false.
     *
     * @param x The x coordinate of the block
     * @param y The y coordinate of the block to check.
     * @param z The z-coordinate of the block.
     * @return A boolean value.
     */
    public boolean contains(int x, int y, int z) {
        return x >= this.x1 && x <= this.x2 && y >= this.y1 && y <= this.y2 && z >= this.z1 && z <= this.z2;
    }

    /**
     * If the world name is the same and the coordinates are within the region, return true.
     *
     * @param location The location to check.
     * @return A boolean value.
     */
    public boolean contains(@NotNull Location location) {
        return this.worldName.equals(location.getWorld().getName()) && this.contains(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }


    /**
     * Returns true if the player is in the region.
     *
     * @param player The player to check.
     * @return A boolean value.
     */
    public boolean contains(@NotNull Player player) {
        return this.contains(player.getLocation());
    }

    public @Nullable World getWorld() {
        if (world == null)
            world = Bukkit.getWorld(worldName);
        return world;
    }

    /**
     * Returns a list of all players inside the region.
     *
     * @return A list of players inside the region.
     */
    @JsonIgnore
    public List<Player> getPlayersInside() {
        List<Player> list = Lists.newArrayList();
        if (getWorld() == null)
            return list;
        for (Player onlinePlayer : getWorld().getPlayers()) {
            if (this.contains(onlinePlayer)) list.add(onlinePlayer);
        }
        return list;
    }

    @JsonIgnore
    public Location calculateCenter() {
        Location loc1 = new Location(getWorld(), getX1(), getY1(), getZ1());
        Location loc2 = new Location(getWorld(), getX2(), getY2(), getZ2());
        return loc1.clone().add(loc2.clone()).multiply(0.5D);
    }


}
