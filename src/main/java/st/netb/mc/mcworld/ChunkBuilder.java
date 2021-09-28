package st.netb.mc.mcworld;


import st.netb.mc.mcworld.datastructs.minecraft.Coord2D;
import st.netb.mc.mcworld.datastructs.raw.ChunkHeightmap;

import java.util.HashMap;
import java.util.Map;

public class ChunkBuilder {

    private Coord2D.Chunk chunkLocation;
    private float[][] surface = new float[Constants.CHUNK_LEN_Z][Constants.CHUNK_LEN_X];
    // maps each location to how many times there has been value insertions
    private Map<Coord2D.Block, Integer> insertions = new HashMap<>();
    private ChunkHeightmap chunkHeightmap = null;


    public ChunkBuilder(Coord2D.Chunk chunkLocation) {
        this.chunkLocation = chunkLocation;
    }

    public Coord2D.Chunk getChunkLocation() {
        return chunkLocation;
    }

    /**
     * Checks whether all locations have been assigned a value at least once
     */
    public boolean isComplete() {
        return insertions.size() == Constants.CHUNK_LEN_X * Constants.CHUNK_LEN_Z;
    }

    public boolean isIncomplete() {
        return !isComplete();
    }

    public void insert(Coord2D.Block blockLocation, float height) {
        // add the value; we'll get the average of all additions later
        surface[blockLocation.getZ()][blockLocation.getX()] += height;
        Integer ins = insertions.computeIfAbsent(blockLocation, key -> 0);
        insertions.put(blockLocation, ins + 1);
    }

    /**
     * Compiles the chunk surface and returns it if all data is ready.
     * Otherwise throws exception.
     * <br>
     * The surface is organized as [y][x] to provide cache-optimizations
     * when iterating by x first (ie line by line).
     *
     * @return The chunk height map
     * @throws Exception The chunk has blocks that haven't been inserted
     */
    public ChunkHeightmap build() throws Exception {

        if (isIncomplete()) {
            throw new Exception("cannot compile chunk surface; chunk has missing data");
        }

        if (chunkHeightmap == null) {
            for (int z = 0; z < Constants.CHUNK_LEN_Z; z++) {
                for (int x = 0; x < Constants.CHUNK_LEN_X; x++) {
                    Coord2D.Block blockLocation = new Coord2D.Block(x, z);
                    int n = insertions.get(blockLocation);
                    surface[z][x] /= n; // get average of all insertions at this location

                    if (surface[z][x] < 0.0f) {
                        surface[z][x] = 0.0f;
                    }
                }
            }

            chunkHeightmap = new ChunkHeightmap(chunkLocation, surface);
        }

        return chunkHeightmap;
    }
}
