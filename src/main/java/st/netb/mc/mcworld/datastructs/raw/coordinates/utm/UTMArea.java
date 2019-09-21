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
    public GeoLocation getMinCoords() {
        return minimum;
    }

    @Override
    public GeoLocation getMaxCoords() {
        return maximum;
    }

    @Override
    public GeoArea makeContainer(GeoArea otherArea) {
        if (!(otherArea instanceof UTMArea)) {
            throw new RuntimeException("tried to cointain non-UTM area within UTM area");
        }
        UTMArea area = (UTMArea) otherArea;

        double northingMin = Math.min(minimum.getN(), area.minimum.getN());
        double eastingMin = Math.min(minimum.getE(), area.minimum.getE());

        double northingMax = Math.max(maximum.getN(), area.maximum.getN());
        double eastingMax = Math.max(maximum.getE(), area.maximum.getE());

        return new UTMArea(
                new UTMLocation(northingMin, eastingMin),
                new UTMLocation(northingMax, eastingMax));
    }

    @Override
    protected WorldGrid asSubGridOf(GeoArea container) {

        WorldGrid subGrid = new WorldGrid(
                this.minimum.getE() - container.getMinCoords().x,
                container.getHeight() - (this.maximum.getN() - container.getMinCoords().y),
                this.getWidth(),
                this.getHeight()
        );

        return subGrid;
    }
}
