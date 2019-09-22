package st.netb.mc.mcworld.datastructs.minecraft.coordinates.referenceframe;

import st.netb.mc.mcworld.datastructs.minecraft.coordinates.MinecraftLocation;
import st.netb.mc.mcworld.datastructs.raw.Tuple;

@FunctionalInterface
public interface ReferenceFrameShifter {

    /**
     * @param instance The location we are reference shifting.
     * @return A tuple where:<br>
     *          first: Location of same type as input, but reference shifted.<br>
     *          second: Location of same type as the reference. Referenced to world.
     */
    Tuple<MinecraftLocation> shift(MinecraftLocation instance);
}
