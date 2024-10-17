package fr.skoupi.extensiveapi.minecraft.liteobjects;

/*  LiteLocation
 *  By: vSKAH <vskahhh@gmail.com>

 * Created with IntelliJ IDEA
 * For the project ExtensiveAPI
 */


import lombok.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode
@ToString

/**
 * LiteLocation
 * LiteLocation is a class that represents a location in the world.
 *
 * But the difference is that it's lighter in memory than a Bukkit Location because she doesn't contains the whole shit.
 */
public class LiteLocation {

    private String worldName;

    private double x;
    private double y;
    private double z;

    public Location toBukkitLocation() {
        return new Location(Bukkit.getWorld(getWorldName()), getX(), getY(), getZ());
    }

    public Location toBukkitLocation(float yaw, float pitch) {
        return new Location(Bukkit.getWorld(getWorldName()), getX(), getY(), getZ(), yaw, pitch);
    }

    public static LiteLocation of(Location location) {
        return new LiteLocation(location.getWorld().getName(), location.getX(), location.getY(), location.getZ());
    }

    public static LiteLocation of(String worldName, double x, double y, double z) {
        return new LiteLocation(worldName, x, y, z);
    }

    public static LiteLocation of(String worldName, String x, String y, String z) {
        return new LiteLocation(worldName, Double.parseDouble(x), Double.parseDouble(y), Double.parseDouble(z));
    }

    @Deprecated(forRemoval = true)
    public static LiteLocation fromBukkitLocation(Location location) {
        return new LiteLocation(location.getWorld().getName(), location.getX(), location.getY(), location.getZ());
    }
}
