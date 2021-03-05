package st.netb.mc.mcworld.bukkit;

import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

import java.util.Random;

/**
 * @author Alexander Sagen
 * @date 2021-03-05
 */
public class WorldGenerator extends ChunkGenerator {

    @Override
    public ChunkData generateChunkData(World world, Random random, int x, int z, BiomeGrid biome) {
        ChunkData chunk = createChunkData(world);
        return chunk;
    }

}
