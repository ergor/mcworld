package st.netb.mc.mcworld.rendering;

import st.netb.mc.mcworld.Constants;

import java.awt.*;

public class ChunkHeightmap {

    public static final int DATA_SZ = Constants.CHUNK_LEN_X * Constants.CHUNK_LEN_Y;

    private int[][] data = new int[Constants.CHUNK_LEN_Y][Constants.CHUNK_LEN_X];
    private Point location;

    public ChunkHeightmap(Point location, float[][] heightmap) {

        this.location = location;

        for (int y = 0; y < Constants.CHUNK_LEN_Y; y++) {
            for (int x = 0; x < Constants.CHUNK_LEN_X; x++) {
                data[y][x] = (int) Math.ceil(heightmap[y][x]);
            }
        }
    }

    public int getHeight(int x, int y) {
        return data[y][x];
    }

    public byte[] getBytes() {
        byte[] bytes = new byte[DATA_SZ];

        for (int y = 0; y < Constants.CHUNK_LEN_Y; y++) {
            for (int x = 0; x < Constants.CHUNK_LEN_X; x++) {
                bytes[(y * Constants.CHUNK_LEN_Y) + x] = (byte) data[y][x];
            }
        }

        return bytes;
    }
}
