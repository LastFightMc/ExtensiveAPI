package fr.skoupi.extensiveapi.minecraft.liteobjects;

/*  LiteLocation
 *  By: vSKAH <vskahhh@gmail.com>

 * Created with IntelliJ IDEA
 * For the project ExtensiveAPI
 */


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    @JsonProperty("world_name")
    private String worldName;

    @JsonProperty("x")
    private double x;

    @JsonProperty("y")
    private double y;

    @JsonProperty("z")
    private double z;

    public @Nullable Location toBukkitLocation() {
        final var world = Bukkit.getWorld(worldName);
        if (world == null)
            return null;
        return new Location(world, getX(), getY(), getZ());
    }

    public @Nullable Location toBukkitLocation(float yaw, float pitch) {
        final var world = Bukkit.getWorld(worldName);
        if (world == null)
            return null;
        return new Location(world, getX(), getY(), getZ(), yaw, pitch);
    }

    public static @NotNull LiteLocation of(@NotNull ConfigurationSection section) throws IllegalArgumentException {
        final String world = section.getString("world");
        if (world == null)
            throw new IllegalArgumentException("The world configuration is missing");

        if (!section.contains("x") || !section.contains("y") || !section.contains("z"))
            throw new IllegalArgumentException("The x and y or z configuration is missing");

        final int x = section.getInt("x");
        final int y = section.getInt("y");
        final int z = section.getInt("z");

        return LiteLocation.of(world, x, y, z);
    }

    public static @NotNull LiteLocation of(@NotNull Location location) {
        return new LiteLocation(location.getWorld().getName(), location.getX(), location.getY(), location.getZ());
    }

    public static @NotNull LiteLocation of(@NotNull String worldName, double x, double y, double z) {
        return new LiteLocation(worldName, x, y, z);
    }

    public static @NotNull LiteLocation of(@NotNull String worldName, @NotNull String x, @NotNull String y, @NotNull String z) {
        return new LiteLocation(worldName, Double.parseDouble(x), Double.parseDouble(y), Double.parseDouble(z));
    }

    @Deprecated(forRemoval = true)
    public static @NotNull LiteLocation fromBukkitLocation(@NotNull Location location) {
        return new LiteLocation(location.getWorld().getName(), location.getX(), location.getY(), location.getZ());
    }
}
