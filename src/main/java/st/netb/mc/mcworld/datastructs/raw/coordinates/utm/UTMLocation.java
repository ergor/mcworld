package st.netb.mc.mcworld.datastructs.raw.coordinates.utm;


import st.netb.mc.mcworld.datastructs.raw.coordinates.GeoLocation;

public class UTMLocation extends GeoLocation {

    public UTMLocation(double northing, double easting) {
        super(easting, northing);
    }

    public double getN() {
        return y;
    }

    public double getE() {
        return x;
    }
}
