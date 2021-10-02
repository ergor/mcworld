package no.netb.mc.mcworld.datastructs.raw;

import no.netb.mc.mcworld.Constants;
import no.netb.mc.mcworld.datastructs.minecraft.Coord2D;

import java.util.Arrays;

public class RegionHeightmap {

    public static final int DATA_SZ = ChunkHeightmap.DATA_SZ * Constants.REGION_LEN_X * Constants.REGION_LEN_Z;

    Coord2D.Region location;
    ChunkHeightmap[][] data = new ChunkHeightmap[Constants.REGION_LEN_Z][Constants.REGION_LEN_X];

    public RegionHeightmap() {
    }

    public RegionHeightmap(Coord2D.Region location, byte[] data) throws Exception {

        if (data.length != DATA_SZ) {
            throw new Exception("input region data length invalid");
        }

        this.location = location;

        for (int z = 0; z < Constants.REGION_LEN_Z; z++) {
            for (int x = 0; x < Constants.REGION_LEN_X; x++) {

                Coord2D.Chunk chunkLocation = new Coord2D.Chunk(x, z);
                int bufPtr = ((Constants.REGION_LEN_Z * z) + x) * ChunkHeightmap.DATA_SZ;

                ChunkHeightmap chunk = new ChunkHeightmap(
                        chunkLocation,
                        Arrays.copyOfRange(data, bufPtr, bufPtr + ChunkHeightmap.DATA_SZ));

                insertChunk(chunk);
            }
        }
    }

    public int getHeight(int x, int z) {

        Coord2D.Relative<Coord2D.Block, Coord2D.Chunk> blockChunkRelative = Coord2D.blockInChunk(new Coord2D.Block(x, z));

        Coord2D.Block blockLocation = blockChunkRelative.getRel();
        Coord2D.Chunk chunkLocation = blockChunkRelative.getAbs();

        return data[chunkLocation.getZ()][chunkLocation.getX()]
                .getHeight(
                        blockLocation.getX(),
                        blockLocation.getZ()
                );
    }

    public ChunkHeightmap getChunk(Coord2D.Chunk chunkLocation) {
        return this.data[chunkLocation.getZ()][chunkLocation.getX()];
    }

    public void insertChunk(ChunkHeightmap chunk) {
        Coord2D.Chunk chunkLocation = chunk.getLocation();
        this.data[chunkLocation.getZ()][chunkLocation.getX()] = chunk;
    }

    public Coord2D.Region getLocation() {
        return location;
    }
}
