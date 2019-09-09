package st.netb.mc.mcworld.datastructs;

import st.netb.mc.mcworld.math.Point3D;

public class Block {

    /**
     * Like in minecraft, the X and Z coordinates are east/west
     * and north/south, while Y is height
     */
    private Point3D location;

    public Block(Point3D location) {
        this.location = location;
    }

    /**
     * Create block from 2D heightmap
     *
     * @param height the height
     * @param x location on east/west axis
     * @param y location on north/south axis
     *
     * @return the block
     */
    public static Block create(float height, int x, int y) {
        return new Block(new Point3D(x, Math.round(height), y));
    }

    public Point3D getLocation() {
        return location;
    }
}
