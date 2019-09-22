package st.netb.mc.mcworld.datastructs.minecraft.coordinates.referenceframe;

import st.netb.mc.mcworld.datastructs.minecraft.coordinates.MinecraftLocation;

public class ReferenceFrameException extends RuntimeException {

    public ReferenceFrameException(String s) {
        super(s);
    }

    public ReferenceFrameException(MinecraftLocation instance, ReferenceFrame toFrame) {
        super(String.format("%s: cannot change reference frame from %s to %s",
                instance.toString(),
                instance.getReferenceFrame().toString(),
                toFrame.toString()));
    }
}
