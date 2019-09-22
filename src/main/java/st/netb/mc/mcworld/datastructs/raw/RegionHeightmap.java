package st.netb.mc.mcworld.datastructs.raw;

import st.netb.mc.mcworld.Constants;
import st.netb.mc.mcworld.coordinates.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Arrays;

public class RegionHeightmap {

    public static final int DATA_SZ = ChunkHeightmap.DATA_SZ * Constants.REGION_LEN_X * Constants.REGION_LEN_Z;

    //int[] data = new int[DATA_SZ];
    ChunkHeightmap[][] data = new ChunkHeightmap[Constants.REGION_LEN_Z][Constants.REGION_LEN_X];

    public RegionHeightmap() {
    }

    public RegionHeightmap(RegionLocation location, byte[] data) throws Exception {

        if (data.length != DATA_SZ) {
            throw new Exception("input region data length invalid");
        }

        for (int z = 0; z < Constants.REGION_LEN_Z; z++) {
            for (int x = 0; x < Constants.REGION_LEN_X; x++) {

                ChunkLocation chunkLocation = new ChunkLocation(x, z, ReferenceFrame.REGION);
                int bufPtr = ((Constants.REGION_LEN_Z * z) + x) * ChunkHeightmap.DATA_SZ;

                ChunkHeightmap chunk = new ChunkHeightmap(
                        chunkLocation,
                        Arrays.copyOfRange(data, bufPtr, bufPtr + ChunkHeightmap.DATA_SZ));

                insertChunk(chunk);
            }
        }
    }

    public int getHeight(int x, int z) {

        Tuple<MinecraftLocation> locationTuple = new BlockLocation(x, z)
                .referencedToChunk();

        BlockLocation blockLocation = (BlockLocation) locationTuple.first();
        ChunkLocation chunkLocation = (ChunkLocation) locationTuple.second();

        return data[chunkLocation.z][chunkLocation.x].getHeight(blockLocation.x, blockLocation.z);
    }

    public void insertChunk(ChunkHeightmap chunk) {
        ChunkLocation chunkLocation = chunk.getLocation();

        if (chunkLocation.isAbsolutePosition()) {
            throw new NotImplementedException();
        }
        else {
            this.data[chunkLocation.z][chunkLocation.x] = chunk;
        }
    }
}
