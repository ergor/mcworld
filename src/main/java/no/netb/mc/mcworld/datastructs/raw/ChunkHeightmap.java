package no.netb.mc.mcworld.datastructs.raw;

import no.netb.mc.mcworld.Constants;
import no.netb.mc.mcworld.datastructs.minecraft.Coord2D;


public class ChunkHeightmap {

    public static final int DATA_SZ = Constants.CHUNK_LEN_X * Constants.CHUNK_LEN_Z;

    private int[][] data = new int[Constants.CHUNK_LEN_Z][Constants.CHUNK_LEN_X];
    private Coord2D.Chunk location;

    public ChunkHeightmap(Coord2D.Chunk location, float[][] heightmap) {

        this.location = location;

        for (int z = 0; z < Constants.CHUNK_LEN_Z; z++) {
            for (int x = 0; x < Constants.CHUNK_LEN_X; x++) {
                data[z][x] = (int) Math.ceil(heightmap[z][x]);
            }
        }
    }

    public ChunkHeightmap(Coord2D.Chunk location, byte[] rawData) {

        this.location = location;

        for (int z = 0; z < Constants.CHUNK_LEN_Z; z++) {
            for (int x = 0; x < Constants.CHUNK_LEN_X; x++) {
                data[z][x] = Byte.toUnsignedInt(rawData[x + z * Constants.CHUNK_LEN_Z]);
            }
        }
    }

    public Coord2D.Chunk getLocation() {
        return location;
    }

    public int getHeight(int x, int z) {
        return data[z][x];
    }

    public byte[] getBytes() {
        byte[] bytes = new byte[DATA_SZ];

        for (int z = 0; z < Constants.CHUNK_LEN_Z; z++) {
            for (int x = 0; x < Constants.CHUNK_LEN_X; x++) {
                bytes[(z * Constants.CHUNK_LEN_Z) + x] = (byte) data[z][x];
            }
        }

        return bytes;
    }
}
