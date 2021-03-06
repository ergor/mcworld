package st.netb.mc.mcworld.datastructs.minecraft.coordinates;

import st.netb.mc.mcworld.datastructs.minecraft.coordinates.referenceframe.FrameTransition;
import st.netb.mc.mcworld.datastructs.minecraft.coordinates.referenceframe.ReferenceFrameShifter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RegionLocation extends MinecraftLocation {

    public RegionLocation(int x, int z) {
        super(x, z);
    }

    private static Map<FrameTransition, ReferenceFrameShifter> referenceShifters =
            Collections.unmodifiableMap(new HashMap<>());

    @Override
    Map<FrameTransition, ReferenceFrameShifter> getReferenceShifters() {
        return referenceShifters;
    }
}
