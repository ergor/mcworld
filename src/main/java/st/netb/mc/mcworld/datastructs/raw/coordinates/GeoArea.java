package st.netb.mc.mcworld.datastructs.raw.coordinates;

import st.netb.mc.mcworld.coordinates.WorldGrid;


/**
 * Interface for all 2D coordinate systems that is agnostic to positive and negative directions
 */
public abstract class GeoArea {

    public abstract double getWidth();
    public abstract double getHeight();
    public abstract GeoLocation getMinCoords();
    public abstract GeoLocation getMaxCoords();
    public abstract GeoArea makeContainer(GeoArea otherArea);

    protected abstract WorldGrid asSubGridOf(GeoArea container);

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

    public boolean contains(GeoArea area) {
        return this.getMinCoords().x <= area.getMinCoords().x
                && this.getMaxCoords().x >= area.getMaxCoords().x
                && this.getMinCoords().y <= area.getMinCoords().y
                && this.getMaxCoords().y >= area.getMaxCoords().y;
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

