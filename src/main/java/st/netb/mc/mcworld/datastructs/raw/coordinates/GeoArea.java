package st.netb.mc.mcworld.datastructs.raw.coordinates;

import st.netb.mc.mcworld.coordinates.WorldGrid;


/**
 * Interface for all 2D coordinate systems that maps to in game coordinate system
 */
public abstract class GeoArea {

    abstract public double getWidth();
    abstract public double getHeight();
    abstract public GeoLocation getOrigin();

    abstract protected WorldGrid asSubGridOf(GeoArea container);

    public static WorldGrid toWorldGrid(GeoArea area) {
        return new WorldGrid(
                0,
                0,
                area.getHeight(),
                area.getWidth()
        );
    }

    public static WorldGrid toWorldGrid(GeoArea area, GeoArea subArea) {
        return subArea.asSubGridOf(area);
    }
}

