package st.netb.mc.mcworld.datastructs.raw;

import st.netb.mc.mcworld.Constants;
import st.netb.mc.mcworld.coordinates.ChunkLocation;


public class ChunkHeightmap {

    public static final int DATA_SZ = Constants.CHUNK_LEN_X * Constants.CHUNK_LEN_Z;

    private int[][] data = new int[Constants.CHUNK_LEN_Z][Constants.CHUNK_LEN_X];
    private ChunkLocation location;

    public ChunkHeightmap(ChunkLocation location, float[][] heightmap) {

        this.location = location;

        for (int y = 0; y < Constants.CHUNK_LEN_Z; y++) {
            for (int x = 0; x < Constants.CHUNK_LEN_X; x++) {
                data[y][x] = (int) Math.ceil(heightmap[y][x]);
            }
        }
    }

    public ChunkLocation getLocation() {
        return location;
    }

    public int getHeight(int x, int y) {
        return data[y][x];
    }

    public byte[] getBytes() {
        byte[] bytes = new byte[DATA_SZ];

        for (int y = 0; y < Constants.CHUNK_LEN_Z; y++) {
            for (int x = 0; x < Constants.CHUNK_LEN_X; x++) {
                bytes[(y * Constants.CHUNK_LEN_Z) + x] = (byte) data[y][x];
            }
        }

        return bytes;
    }
}
