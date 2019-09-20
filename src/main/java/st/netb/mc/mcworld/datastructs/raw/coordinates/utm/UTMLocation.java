package st.netb.mc.mcworld.datastructs.raw.coordinates.utm;


import java.awt.geom.Point2D;

public class UTMLocation {

    private double northing;
    private double easting;

    public UTMLocation(double northing, double easting) {
        this.northing = northing;
        this.easting = easting;
    }

    public double getN() {
        return northing;
    }

    public double getE() {
        return easting;
    }
}
