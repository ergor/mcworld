package st.netb.mc.mcworld.datastructs.raw;


import st.netb.mc.mcworld.datastructs.raw.coordinates.GeoArea;
import st.netb.mc.mcworld.datastructs.raw.coordinates.utm.UTMLocation;

import java.awt.image.Raster;
import java.util.function.Supplier;

public class WorldSection {

    private double resolution; // meters per pixel
    private GeoArea area;
    private Supplier<Raster> rasterSupplier; // save memory by not loading every raster all at once

    private WorldSection(double resolution, GeoArea area, Supplier<Raster> rasterSupplier) {
        this.resolution = resolution;
        this.area = area;
        this.rasterSupplier = rasterSupplier;
    }

    public WorldSection(
            Supplier<Raster> rasterSupplier,
            double resolution,
            double northingMin,
            double northingMax,
            double eastingMin,
            double eastingMax) {

        /*
         x – the X coordinate of the upper-left corner of the newly constructed Rectangle2D
         y – the Y coordinate of the upper-left corner of the newly constructed Rectangle2D
         w – the width of the newly constructed Rectangle2D
         h – the height of the newly constructed Rectangle2D
         */

        UTMLocation minimum = new UTMLocation(northingMin, eastingMin);
        UTMLocation maximum = new UTMLocation(northingMax, eastingMax);

        area = new GeoArea(minimum, maximum);

        this.rasterSupplier = rasterSupplier;
        this.resolution = resolution;
    }

    public GeoArea getArea() {
        return area;
    }

    public WorldSection mapArea(GeoArea area) {
        return new WorldSection(
              this.resolution,
              area,
              this.rasterSupplier
        );
    }

    public Raster getRaster() {
        return rasterSupplier.get();
    }

    /**
     * Meters per pixel
     */
    public double getResolution() {
        return resolution;
    }
}
