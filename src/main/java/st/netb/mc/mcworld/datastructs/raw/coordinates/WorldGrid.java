package st.netb.mc.mcworld.datastructs.raw.coordinates;

import java.awt.geom.Rectangle2D;

/**
 *
 */
public class WorldGrid extends Rectangle2D.Double {
    /**
     * Constructs and initializes a <code>Rectangle2D</code>
     * from the specified <code>double</code> coordinates.
     *
     * @param x the X coordinate of the upper-left corner
     *          of the newly constructed <code>Rectangle2D</code>
     * @param y the Y coordinate of the upper-left corner
     *          of the newly constructed <code>Rectangle2D</code>
     * @param w the width of the newly constructed
     *          <code>Rectangle2D</code>
     * @param h the height of the newly constructed
     *          <code>Rectangle2D</code>
     * @since 1.2
     */
    public WorldGrid(double x, double y, double w, double h) {
        super(x, y, w, h);
    }
}
