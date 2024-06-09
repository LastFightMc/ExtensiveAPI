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
public class LiteLocation {

    private String worldName;

    private double x;
    private double y;
    private double z;

    public Location toBukkitLocation() {
        return new Location(Bukkit.getWorld(getWorldName()), getX(), getY(), getZ());
    }

    public LiteLocation fromBukkitLocation(Location location) {
        this.setWorldName(location.getWorld().getName());
        this.setX(location.getX());
        this.setY(location.getY());
        this.setZ(location.getZ());
        return this;
    }
}
