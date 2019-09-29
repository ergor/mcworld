package st.netb.mc.mcworld.datastructs.raw.coordinates;


import java.awt.geom.Rectangle2D;

/**
 * Base class for all 2D coordinate systems that is agnostic to positive and negative directions.
 * Used as an intermediate step between the original coordinate system and the one used in rendering.
 *
 * @see WorldGrid
 */
public class GeoArea {

    CoordinateSystem coordinateSystem;
    Coordinate topLeft;
    Coordinate bottomRight;
    Coordinate minimum;
    Coordinate maximum;

    private GeoArea() {

    }

    public static GeoArea create(CoordinateSystem coordinateSystem, Coordinate minimum, Coordinate maximum) {
        GeoArea geoArea = new GeoArea();
        geoArea.coordinateSystem = coordinateSystem;
        geoArea.topLeft = topLeft;
        geoArea.bottomRight = bottomRight;
        geoArea.minimum = minimum;
        geoArea.maximum = maximum;

    }

    public Coordinate getMinCoords() {
        return minimum;
    }
    public Coordinate getMaxCoords() {
        return maximum;
    }

    public double getHeight() {
        return bottomRight.y - topLeft.y;
    }

    public double getWidth() {
        return bottomRight.x - topLeft.x;
    }

    public GeoArea makeContainer(GeoArea otherArea) {
        double minX = Math.min(minimum.x, otherArea.minimum.x);
        double minY = Math.min(minimum.y, otherArea.minimum.y);

        double maxX = Math.max(maximum.x, otherArea.maximum.x);
        double maxY = Math.max(maximum.y, otherArea.maximum.y);

        return coordinateSystem.transform(new Coordinate(minX, minY), new Coordinate(maxX, maxY));
    }

    @Override
    public String toString() {
        return String.format("%s, %s; w=%f, h=%f",
                getMinCoords().toString(),
                getMaxCoords().toString(),
                getWidth(),
                getHeight());
    }
}

