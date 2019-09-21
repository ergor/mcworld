package st.netb.mc.mcworld.coordinates;

import st.netb.mc.mcworld.Constants;
import st.netb.mc.mcworld.datastructs.raw.Tuple;

public class ChunkLocation extends MinecraftLocation {

    public ChunkLocation(int x, int z) {
        super(x, z);
    }

    public Tuple<MinecraftLocation> referencedToRegion() {
        int chunkX = x % Constants.REGION_LEN_X;
        int chunkZ = z % Constants.REGION_LEN_Z;

        int regionX = x / Constants.REGION_LEN_X;
        int regionZ = z / Constants.REGION_LEN_Z;

        return new Tuple<>(
                new ChunkLocation(chunkX, chunkZ),
                new RegionLocation(regionX, regionZ)
        );
    }
}
