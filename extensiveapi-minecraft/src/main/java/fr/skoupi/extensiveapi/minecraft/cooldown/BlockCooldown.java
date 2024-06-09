package fr.skoupi.extensiveapi.minecraft.cooldown;

/*  BlockCooldown
 *  By: vSKAH <vskahhh@gmail.com>

 * Created with IntelliJ IDEA
 * For the project ExtensiveAPI
 */

import fr.skoupi.extensiveapi.core.cooldown.Cooldown;
import fr.skoupi.extensiveapi.minecraft.ExtensiveCore;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.block.Block;
import org.bukkit.metadata.FixedMetadataValue;

public class BlockCooldown implements Cooldown<Block> {
    @Override
    // It's adding a timer to the block.
    public void addTimer(Block block, String cooldownIdentifier, long time) {
        block.setMetadata(cooldownIdentifier, new FixedMetadataValue(ExtensiveCore.getInstance(), (time * 1000) + System.currentTimeMillis()));
    }

    @Override
    // It's checking if the player has a timer.
    public boolean isInTimer(Block block, String cooldownIdentifier) {
        return block.hasMetadata(cooldownIdentifier) && block.getMetadata(cooldownIdentifier).size() > 0 && block.getMetadata(cooldownIdentifier).get(0).asLong() > System.currentTimeMillis();
    }

    @Override
    // It's getting the time left of the timer.
    public long getTime(Block block, String cooldownIdentifier) {
        return !block.hasMetadata(cooldownIdentifier) ? 0 : (block.getMetadata(cooldownIdentifier).get(0).asLong() - System.currentTimeMillis()) / 1000;
    }

    /**
     * It returns a formatted string of the time left on the cooldown
     *
     * @param object The object that the cooldown is being applied to.
     * @param cooldownIdentifier The identifier of the cooldown.
     * @return A string of the time left in the format of H'h 'm'm 's's'
     */
    public String getFormattedTime(Block object, String cooldownIdentifier) {
        return DurationFormatUtils.formatDuration(getTime(object, cooldownIdentifier) * 1000, "H'h 'm'm 's's'");
    }
}
