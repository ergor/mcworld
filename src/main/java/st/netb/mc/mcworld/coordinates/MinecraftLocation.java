package st.netb.mc.mcworld.coordinates;

public abstract class MinecraftLocation {

    public int x;
    public int z;

    public MinecraftLocation(int x, int z) {
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

    @Override
    public String toString() {
        return "(" + x + ", " + z + ")";
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
