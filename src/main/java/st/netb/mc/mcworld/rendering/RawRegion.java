package st.netb.mc.mcworld.rendering;

import st.netb.mc.mcworld.Constants;

import java.awt.*;

public class RawRegion {

    public static final int DATA_SZ = ChunkHeightmap.DATA_SZ * Constants.REGION_LEN_X * Constants.REGION_LEN_Y;

    int[] data = new int[DATA_SZ];

    public RawRegion() {
    }

    public RawRegion(byte[] data) throws Exception {

        if (data.length != DATA_SZ) {
            throw new Exception("input region data length invalid");
        }

        for (int i = 0; i < DATA_SZ; i++) {
            this.data[i] = Byte.toUnsignedInt(data[i]);
        }
    }

    public int getHeight(int x, int y) {
        return 0;
    }

    public void insertChunk(Point location, ChunkHeightmap chunk) {

    }
}
