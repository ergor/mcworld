package st.netb.mc.mcworld.datastructs.raw;

import st.netb.mc.mcworld.Constants;
import st.netb.mc.mcworld.datastructs.minecraft.coordinates.*;
import st.netb.mc.mcworld.datastructs.minecraft.coordinates.referenceframe.ReferenceFrame;

import java.util.Arrays;

public class RegionHeightmap {

    public static final int DATA_SZ = ChunkHeightmap.DATA_SZ * Constants.REGION_LEN_X * Constants.REGION_LEN_Z;

    RegionLocation location;
    ChunkHeightmap[][] data = new ChunkHeightmap[Constants.REGION_LEN_Z][Constants.REGION_LEN_X];

    public RegionHeightmap() {
    }

    public RegionHeightmap(RegionLocation location, byte[] data) throws Exception {

        if (data.length != DATA_SZ) {
            throw new Exception("input region data length invalid");
        }

        this.location = location;

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
                .tryReferencedTo(ReferenceFrame.CHUNK);

        BlockLocation blockLocation = (BlockLocation) locationTuple.first();
        ChunkLocation chunkLocation = (ChunkLocation) locationTuple.second();

        return data[chunkLocation.getZ()][chunkLocation.getX()]
                .getHeight(
                        blockLocation.getX(ReferenceFrame.CHUNK),
                        blockLocation.getZ(ReferenceFrame.CHUNK));
    }

    public void insertChunk(ChunkHeightmap chunk) {
        ChunkLocation chunkLocation = chunk.getLocation();

        this.data[chunkLocation.getZ(ReferenceFrame.REGION)]
                 [chunkLocation.getX(ReferenceFrame.REGION)] = chunk;
    }

    public RegionLocation getLocation() {
        return location;
    }
}
