package st.netb.mc.mcworld.datastructs.raw.coordinates;

@FunctionalInterface
public interface Transformer {

    /**
     * TODO: incorrect; update this description
     * For the given extrema of an area in an arbitrary coordinate system,
     * shall transform the coordinate system such that:
     * <ul>
     * <li>minimum corresponds to the <i>top left</i> corner</li>
     * <li>maximum corresponds to the <i>bottom right</i> corner</li>
     * </ul>
     */
    GeoArea transform(Coordinate minimum, Coordinate maximum);
}
