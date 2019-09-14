package st.netb.mc.mcworld.datastructs.raw;


import java.awt.geom.Rectangle2D;
import java.awt.image.Raster;

public class WorldSection {

    private double resolution; // meters per pixel
    private Rectangle2D.Double area;
    private Raster raster;

    public WorldSection(Raster raster, double northingMin, double northingMax, double eastingMin, double eastingMax) {

        /*
         x – the X coordinate of the upper-left corner of the newly constructed Rectangle2D
         y – the Y coordinate of the upper-left corner of the newly constructed Rectangle2D
         w – the width of the newly constructed Rectangle2D
         h – the height of the newly constructed Rectangle2D
         */

        double x = eastingMin;
        double y = northingMin;

        double w = eastingMax - eastingMin;
        double h = northingMax - northingMin;

        area = new Rectangle2D.Double(x, y, w, h);

        this.raster = raster;
        this.resolution = area.width / raster.getWidth();
    }

    public Rectangle2D.Double getArea() {
        return area;
    }

    public Raster getRaster() {
        return raster;
    }

    /**
     * Meters per pixel
     */
    public double getResolution() {
        return resolution;
    }
}
