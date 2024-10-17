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

/**
 * LiteChunk
 * LiteChunk is a class that represents a chunk in the world.
 *
 * But the difference is that it's lighter in memory than a Bukkit Chunk because she doesn't contains the whole shit.
 */
public class LiteChunk {

    private String worldName;

    private int chunkX, chunkZ;

    public static LiteChunk of(Chunk chunk) {
        return new LiteChunk(chunk.getWorld().getName(), chunk.getX(), chunk.getZ());
    }

    public static LiteChunk of(String worldName, int chunkX, int chunkZ) {
        return new LiteChunk(worldName, chunkX, chunkZ);
    }

    public static LiteChunk of(String worldName, String chunkX, String chunkZ) {
        return new LiteChunk(worldName, Integer.parseInt(chunkX), Integer.parseInt(chunkZ));
    }

    public CompletableFuture<Chunk> toBukkitChunk(boolean isUrgent) {
        return PaperLib.getChunkAtAsync(Bukkit.getWorld(getWorldName()), getChunkX(), getChunkZ(), true, isUrgent);
    }


    @Deprecated(forRemoval = true)
    public LiteChunk fromBukkitChunk(Chunk chunk) {
        this.setWorldName(chunk.getWorld().getName());
        this.setChunkX(chunk.getX());
        this.setChunkZ(chunk.getZ());
        return this;
    }
}
