package st.netb.mc.mcworld.datastructs.raw;

import java.awt.geom.Rectangle2D;

public class GeoArea extends Rectangle2D.Double {

    public GeoArea(double x, double y, double w, double h) {
        super(x, y, w, h);
    }

    /**
     * UTM has origin in bottom left, while a rectangle has origin top left.
     * This constructor will create a GeoArea that follows rectangle convention (ie top left)
     * by converting UTM -> rectangle
     * @param minimum the UTM containing the lowest values
     * @param maximum the UTM containing the highest values
     */
    public GeoArea(UTM minimum, UTM maximum) {
        double x0 = minimum.getE();
        double y0 = maximum.getN();

        double x1 = maximum.getE();
        double y1 = minimum.getN();

        this.x = x0;
        this.y = y0;
        this.width = x1 - x0;
        this.height = y1 - y0; // height is inverted between UTM and rectangle
    }
}

