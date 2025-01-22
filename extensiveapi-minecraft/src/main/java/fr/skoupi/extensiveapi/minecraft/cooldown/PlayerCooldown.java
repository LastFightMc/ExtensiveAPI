package fr.skoupi.extensiveapi.minecraft.cooldown;

/*  PlayerCooldown
 *  By: vSKAH <vskahhh@gmail.com>

 * Created with IntelliJ IDEA
 * For the project ExtensiveAPI
 */

import fr.skoupi.extensiveapi.core.cooldown.Cooldown;
import fr.skoupi.extensiveapi.minecraft.ExtensiveCore;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.NotNull;

public class PlayerCooldown implements Cooldown<Player> {
    @Override
    public void addMsTimer(@NotNull Player player, @NotNull String cooldownIdentifier, long timeInMs) {
        player.setMetadata(cooldownIdentifier, new FixedMetadataValue(ExtensiveCore.getInstance(), timeInMs + System.currentTimeMillis()));
    }

    @Override
    // It's adding a timer to the player.
    public void addTimer(@NotNull Player player, @NotNull String cooldownIdentifier, long time) {
        player.setMetadata(cooldownIdentifier, new FixedMetadataValue(ExtensiveCore.getInstance(), (time * 1000) + System.currentTimeMillis()));
    }

    @Override
    // It's checking if the player has a timer.
    public boolean isInTimer(@NotNull Player player, @NotNull String cooldownIdentifier) {
        return player.hasMetadata(cooldownIdentifier) && player.getMetadata(cooldownIdentifier).size() > 0 && player.getMetadata(cooldownIdentifier).get(0).asLong() > System.currentTimeMillis();
    }

    @Override
    // It's getting the time left of the timer.
    public long getTime(@NotNull Player player, @NotNull String cooldownIdentifier) {
        return !player.hasMetadata(cooldownIdentifier) ? 0 : (player.getMetadata(cooldownIdentifier).get(0).asLong() - System.currentTimeMillis()) / 1000;
    }

    public @NotNull String getFormattedTime(@NotNull Player player, String cooldownIdentifier) {
        return DurationFormatUtils.formatDuration(getTime(player, cooldownIdentifier) * 1000, "H'h 'm'm 's's'");
    }

    public @NotNull String getFormattedTime(@NotNull Player player, String cooldownIdentifier, String format) {
        return DurationFormatUtils.formatDuration(getTime(player, cooldownIdentifier) * 1000, format);
    }
}
