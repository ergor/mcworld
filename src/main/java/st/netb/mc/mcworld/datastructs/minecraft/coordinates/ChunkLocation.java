package st.netb.mc.mcworld.datastructs.minecraft.coordinates;

import st.netb.mc.mcworld.Constants;
import st.netb.mc.mcworld.datastructs.minecraft.coordinates.referenceframe.FrameTransition;
import st.netb.mc.mcworld.datastructs.minecraft.coordinates.referenceframe.ReferenceFrame;
import st.netb.mc.mcworld.datastructs.minecraft.coordinates.referenceframe.ReferenceFrameShifter;
import st.netb.mc.mcworld.datastructs.raw.Tuple;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ChunkLocation extends MinecraftLocation {

    public ChunkLocation(int x, int z) {
        super(x, z);
    }

    public ChunkLocation(int x, int z, ReferenceFrame referenceFrame) {
        super(x, z, referenceFrame);
    }

    private static Map<FrameTransition, ReferenceFrameShifter> referenceShifters;
    static {
        Map<FrameTransition, ReferenceFrameShifter> map = new HashMap<>();

        map.put(new FrameTransition(ReferenceFrame.WORLD, ReferenceFrame.REGION),
                ChunkLocation::worldToRegionReferenceShifter);

        referenceShifters = Collections.unmodifiableMap(map);
    }

    @Override
    Map<FrameTransition, ReferenceFrameShifter> getReferenceShifters() {
        return referenceShifters;
    }

    private static Tuple<MinecraftLocation> worldToRegionReferenceShifter(MinecraftLocation instance) {
        int chunkX = instance.x % Constants.REGION_LEN_X;
        int chunkZ = instance.z % Constants.REGION_LEN_Z;

        int regionX = instance.x / Constants.REGION_LEN_X;
        int regionZ = instance.z / Constants.REGION_LEN_Z;

        return new Tuple<>(
                new ChunkLocation(chunkX, chunkZ, ReferenceFrame.REGION),
                new RegionLocation(regionX, regionZ)
        );
    }
}
