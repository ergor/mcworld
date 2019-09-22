package st.netb.mc.mcworld.datastructs.raw.coordinates;


/**
 * Base class for all 2D coordinate systems that is agnostic to positive and negative directions.
 * Used as an intermediate step between the original coordinate system and the one used in rendering.
 *
 * @see WorldGrid
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

