package st.netb.mc.mcworld.datastructs.minecraft.coordinates;

import st.netb.mc.mcworld.datastructs.raw.Tuple;

import java.util.function.Supplier;

public abstract class MinecraftLocation {

    public int x;
    public int z;

    ReferenceFrame referenceFrame;

    public MinecraftLocation(int x, int z) {
        this.referenceFrame = ReferenceFrame.WORLD;
        this.x = x;
        this.z = z;
    }

    public MinecraftLocation(int x, int z, ReferenceFrame referenceFrame) {
        this.referenceFrame = referenceFrame;
        this.x = x;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public ReferenceFrame getReferenceFrame() {
        return referenceFrame;
    }

    public boolean isAbsolutePosition() {
        return referenceFrame == ReferenceFrame.WORLD;
    }

    public boolean isRelativePosition() {
        return !isAbsolutePosition();
    }

    Tuple<MinecraftLocation> shiftReference(Supplier<Tuple<MinecraftLocation>> shifter) {

        if (isRelativePosition()) {
            throw new RuntimeException("MinecraftLocation: can only shift reference if referenced to world");
        }

        return shifter.get();
    }

    @Override
    public String toString() {
        return "(" + x + ", " + z + ") $" + referenceFrame.toString();
    }


    /**
     * Stolen from {@link java.awt.geom.Point2D#hashCode()}
     */
    @Override
    public int hashCode() {
        long bits = Double.doubleToLongBits(x);
        bits ^= Double.doubleToLongBits(z) * 31;
        return (((int) bits) ^ ((int) (bits >> 32)));
    }

    /**
     * Stolen from {@link java.awt.geom.Point2D#equals(Object)}
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MinecraftLocation) {
            MinecraftLocation that = (MinecraftLocation) obj;
            return (x == that.x) && (z == that.z);
        }
        return super.equals(obj);
    }
}
