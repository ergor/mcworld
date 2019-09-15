package st.netb.mc.mcworld.datastructs.raw;

import st.netb.mc.mcworld.Constants;

import java.awt.*;

public class RegionHeightmap {

    public static final int DATA_SZ = ChunkHeightmap.DATA_SZ * Constants.REGION_LEN_X * Constants.REGION_LEN_Z;

    //int[] data = new int[DATA_SZ];
    ChunkHeightmap[][] data = new ChunkHeightmap[Constants.REGION_LEN_Z][Constants.REGION_LEN_X];

    public RegionHeightmap() {
    }

    public RegionHeightmap(Point location, byte[] data) throws Exception {

        if (data.length != DATA_SZ) {
            throw new Exception("input region data length invalid");
        }

        for (int i = 0; i < DATA_SZ; i++) {
            //this.data[i] = Byte.toUnsignedInt(data[i]);
        }
    }

    public int getHeight(int x, int y) {
        return 0;
    }

    public void insertChunk(ChunkHeightmap chunk) {

    }
}
