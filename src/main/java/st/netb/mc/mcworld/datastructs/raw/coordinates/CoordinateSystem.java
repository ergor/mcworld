package st.netb.mc.mcworld.datastructs.raw.coordinates;

public enum CoordinateSystem {

    UTM_NORTH(CoordinateSystem::invertedY);


    Transformer transformer;

    CoordinateSystem(Transformer transformer) {
        this.transformer = transformer;
    }

    public GeoArea transform(Coordinate minimum, Coordinate maximum) {
        return transformer.transform(minimum, maximum);
    }


    /**
     * Mapping:
     * x -> x
     * y -> inverted y
     */
    static GeoArea invertedY(Coordinate minimum, Coordinate maximum) {
        Coordinate topLeft = new Coordinate(minimum.x, maximum.y);
        Coordinate bottomRight = new Coordinate(maximum.x, minimum.y);
        return new GeoArea(topLeft, bottomRight, minimum, maximum);
    }
}
