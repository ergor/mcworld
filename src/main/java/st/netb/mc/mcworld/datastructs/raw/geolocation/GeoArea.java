package st.netb.mc.mcworld.datastructs.raw.geolocation;


import st.netb.mc.mcworld.datastructs.raw.Tuple;

/**
 * A 2D plane that represents a geographical area.
 * The <i>origin</i> is defined as the top left corner, and the <i>extent</i>
 * is defined as the bottom right corner.
 * <br/><br/>
 * <b>Note:</b>
 * the minimum coordinate does not necessarily correspond to the origin,
 * and likewise, the maximum coordinate does not necessarily correspond to the extent.
 * The relationship between these are defined by a {@link CoordinateSystem}.
 * Thus, the width and height of an area <i>can be negative</i>.
 *
 * @see CoordinateSystem
 */
public class GeoArea {

    /* Defines the relationship between minimum/maximum and origin/extent. */
    private CoordinateSystem coordinateSystem;

    /* Not exposed to outside. Only used internal for calculations. */
    private Coordinate minimum;
    private Coordinate maximum;

    /* Exposed via getters. */
    private Coordinate origin;
    private Coordinate extent;

    public GeoArea(CoordinateSystem coordinateSystem, Coordinate minimum, Coordinate maximum) {
        this.minimum = minimum;
        this.maximum = maximum;

        this.coordinateSystem = coordinateSystem;
        Tuple<Coordinate> mappedExtrema = coordinateSystem.map(minimum, maximum);
        this.origin = mappedExtrema.first();
        this.extent = mappedExtrema.second();
    }

    /**
     * The coordinate that is the top left corner
     */
    public Coordinate getOrigin() {
        return origin;
    }

    /**
     * The coordinate that is the bottom right corner
     */
    public Coordinate getExtent() {
        return extent;
    }

    public double getHeight() {
        return extent.y - origin.y;
    }

    public double getWidth() {
        return extent.x - origin.x;
    }

    public CoordinateSystem getCoordinateSystem() {
        return coordinateSystem;
    }

    public GeoArea makeContainer(GeoArea otherArea) {

        if (this.coordinateSystem != otherArea.coordinateSystem) {
            throw new RuntimeException(String.format(
                    "makeContainer: cannot operate on differing coordinate systems (%s vs. %s)",
                    this.coordinateSystem,
                    otherArea.coordinateSystem));
        }

        double minX = Math.min(minimum.x, otherArea.minimum.x);
        double minY = Math.min(minimum.y, otherArea.minimum.y);

        double maxX = Math.max(maximum.x, otherArea.maximum.x);
        double maxY = Math.max(maximum.y, otherArea.maximum.y);

        return new GeoArea(coordinateSystem, new Coordinate(minX, minY), new Coordinate(maxX, maxY));
    }

    public boolean contains(GeoArea otherArea) {
        return this.minimum.x <= otherArea.minimum.x
                && this.maximum.x >= otherArea.maximum.x
                && this.minimum.y <= otherArea.minimum.y
                && this.maximum.y >= otherArea.maximum.y;
    }

    @Override
    public String toString() {
        return String.format("%s, %s; w=%f, h=%f; %s",
                minimum.toString(),
                maximum.toString(),
                getWidth(),
                getHeight(),
                coordinateSystem.toString());
    }
}

