package st.netb.mc.mcworld.datastructs.raw.coordinates.utm;

import st.netb.mc.mcworld.coordinates.WorldGrid;
import st.netb.mc.mcworld.datastructs.raw.coordinates.GeoArea;
import st.netb.mc.mcworld.datastructs.raw.coordinates.GeoLocation;


/**
 * UTM mapping:
 * easting -> x
 * northing -> y inversed
 */
public class UTMArea extends GeoArea {

    UTMLocation minimum;
    UTMLocation maximum;

    public UTMArea(UTMLocation minimum, UTMLocation maximum) {
        this.minimum = minimum;
        this.maximum = maximum;
    }

    @Override
    public double getWidth() {
        return maximum.getE() - minimum.getE();
    }

    @Override
    public double getHeight() {
        return maximum.getN() - minimum.getN();
    }

    @Override
    public GeoLocation getOrigin() {
        return new GeoLocation(minimum.getE(), minimum.getN());
    }

    @Override
    protected WorldGrid asSubGridOf(GeoArea container) {
        WorldGrid subGrid = new WorldGrid(
                minimum.getE(),
                container.getHeight() - maximum.getN(),
                getWidth(),
                getHeight()
        );

        return subGrid;
    }
}
