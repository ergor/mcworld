package no.netb.mc.mcworld.datastructs.raw;


import no.netb.mc.mcworld.datastructs.raw.geolocation.GeoArea;

import java.awt.image.Raster;
import java.util.function.Supplier;

public class WorldSection {

    private Supplier<Raster> rasterSupplier; // save memory by not loading every raster all at once
    private double resX; // distance unit per pixel, can be negative
    private double resY; // distance unit per pixel, can be negative
    private GeoArea area;

    public WorldSection(Supplier<Raster> rasterSupplier, double resX, double resY, GeoArea area) {
        this.rasterSupplier = rasterSupplier;
        this.resX = resX;
        this.resY = resY;
        this.area = area;
    }

    public GeoArea getArea() {
        return area;
    }

    public Raster getRaster() {
        return rasterSupplier.get();
    }

    public double getResX() {
        return resX;
    }

    public double getResY() {
        return resY;
    }
}
