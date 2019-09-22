package st.netb.mc.mcworld.datastructs.minecraft.coordinates;

import st.netb.mc.mcworld.Constants;
import st.netb.mc.mcworld.datastructs.minecraft.coordinates.referenceframe.FrameTransition;
import st.netb.mc.mcworld.datastructs.minecraft.coordinates.referenceframe.ReferenceFrame;
import st.netb.mc.mcworld.datastructs.minecraft.coordinates.referenceframe.ReferenceFrameShifter;
import st.netb.mc.mcworld.datastructs.raw.Tuple;

import java.util.*;

public class BlockLocation extends MinecraftLocation {

    public BlockLocation(int x, int z) {
        super(x, z);
    }

    public BlockLocation(int x, int z, ReferenceFrame referenceFrame) {
        super(x, z, referenceFrame);
    }

    private static Map<FrameTransition, ReferenceFrameShifter> referenceShifters;
    static {
        Map<FrameTransition, ReferenceFrameShifter> map = new HashMap<>();

        map.put(new FrameTransition(ReferenceFrame.WORLD, ReferenceFrame.CHUNK),
                BlockLocation::worldToChunkReferenceShifter);

        referenceShifters = Collections.unmodifiableMap(map);
    }

    @Override
    Map<FrameTransition, ReferenceFrameShifter> getReferenceShifters() {
        return referenceShifters;
    }

    private static Tuple<MinecraftLocation> worldToChunkReferenceShifter(MinecraftLocation instance) {
        int blockX = instance.x % Constants.CHUNK_LEN_X;
        int blockZ = instance.z % Constants.CHUNK_LEN_Z;

        int chunkX = instance.x / Constants.CHUNK_LEN_X;
        int chunkZ = instance.z / Constants.CHUNK_LEN_Z;

        return new Tuple<>(
                new BlockLocation(blockX, blockZ, ReferenceFrame.CHUNK),
                new ChunkLocation(chunkX, chunkZ)
        );
    }
}
