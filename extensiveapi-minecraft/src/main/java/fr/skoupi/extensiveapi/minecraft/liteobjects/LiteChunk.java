package fr.skoupi.extensiveapi.minecraft.liteobjects;

/*  LiteChunk
 *  By: vSKAH <vskahhh@gmail.com>

 * Created with IntelliJ IDEA
 * For the project ExtensiveAPI
 */


import io.papermc.lib.PaperLib;
import lombok.*;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;

import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class LiteChunk {

    private String worldName;

    private int chunkX, chunkZ;

    public LiteChunk fromBukkitChunk(Chunk chunk) {
        this.setWorldName(chunk.getWorld().getName());
        this.setChunkX(chunk.getX());
        this.setChunkZ(chunk.getZ());
        return this;
    }

    public CompletableFuture<Chunk> toBukkitChunk(boolean isUrgent) {
        return PaperLib.getChunkAtAsync(Bukkit.getWorld(getWorldName()), getChunkX(), getChunkZ(), true, isUrgent);
    }

}
