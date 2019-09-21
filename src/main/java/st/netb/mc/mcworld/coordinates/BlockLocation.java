package st.netb.mc.mcworld.coordinates;

import st.netb.mc.mcworld.Constants;
import st.netb.mc.mcworld.datastructs.raw.Tuple;

public class BlockLocation extends MinecraftLocation {

    public BlockLocation(int x, int z) {
        super(x, z);
    }

    /**
     * Gets the {@link ChunkLocation} this block is withing,
     * and the block's location within that chunk
     * @return
     */
    public Tuple<MinecraftLocation> referencedToChunk() {

        int blockX = x % Constants.CHUNK_LEN_X;
        int blockZ = z % Constants.CHUNK_LEN_Z;

        int chunkX = x / Constants.CHUNK_LEN_X;
        int chunkZ = z / Constants.CHUNK_LEN_Z;

        return new Tuple<>(
                new BlockLocation(blockX, blockZ),
                new ChunkLocation(chunkX, chunkZ)
        );
    }
}
