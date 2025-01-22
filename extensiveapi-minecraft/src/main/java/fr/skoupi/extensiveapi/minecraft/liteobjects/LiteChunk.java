package fr.skoupi.extensiveapi.minecraft.liteobjects;

/*  LiteChunk
 *  By: vSKAH <vskahhh@gmail.com>

 * Created with IntelliJ IDEA
 * For the project ExtensiveAPI
 */


import fr.skoupi.extensiveapi.minecraft.ExtensiveCore;
import io.papermc.lib.PaperLib;
import lombok.*;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    public static @NotNull LiteChunk of(@NotNull Chunk chunk) {
        return new LiteChunk(chunk.getWorld().getName(), chunk.getX(), chunk.getZ());
    }

    public static @NotNull LiteChunk of(@NotNull String worldName, int chunkX, int chunkZ) {
        return new LiteChunk(worldName, chunkX, chunkZ);
    }

    public static @NotNull LiteChunk of(@NotNull String worldName, @NotNull String chunkX, @NotNull String chunkZ) {
        return new LiteChunk(worldName, Integer.parseInt(chunkX), Integer.parseInt(chunkZ));
    }

    public @NotNull CompletableFuture<@Nullable Chunk> toBukkitChunk(boolean isUrgent) {
        final World world = Bukkit.getWorld(worldName);
        if (world == null) {
            final var logger = ExtensiveCore.getInstance().getLogger();
            logger.severe("WARNING: toBukkitChunk, world is null");
            return CompletableFuture.completedFuture(null);
        }
        return PaperLib.getChunkAtAsync(world, getChunkX(), getChunkZ(), true, isUrgent);
    }


    @Deprecated(forRemoval = true)
    public LiteChunk fromBukkitChunk(Chunk chunk) {
        this.setWorldName(chunk.getWorld().getName());
        this.setChunkX(chunk.getX());
        this.setChunkZ(chunk.getZ());
        return this;
    }
}
