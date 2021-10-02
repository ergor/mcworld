package no.netb.mc.mcworld.datastructs.raw.geolocation;

/**
 * https://en.wikipedia.org/wiki/Geodetic_datum
 *
 * TL;DR: a reference system for a coordinate system.
 *
 * @see CoordinateSystem
 */
public enum GeodeticDatum {
    WGS84,
    EUREF89,
    ED50
}
