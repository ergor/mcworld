package no.netb.mc.mcworld.datasource;

import no.netb.mc.mcworld.datastructs.raw.WorldSection;

import java.util.List;


/**
 * <a href="https://en.wikipedia.org/wiki/World_file">https://en.wikipedia.org/wiki/World_file</a>
 *
 * <p>
 * The generic meaning of the six parameters in a world file (as defined by Esri[1]) is:
 * <ul>
 *      <li>Line 1: A: pixel size in the x-direction in map units/pixel</li>
 *      <li>Line 2: D: rotation about y-axis</li>
 *      <li>Line 3: B: rotation about x-axis</li>
 *      <li>Line 4: E: pixel size in the y-direction in map units, almost always negative[3]</li>
 *      <li>Line 5: C: x-coordinate of the center of the upper left pixel</li>
 *      <li>Line 6: F: y-coordinate of the center of the upper left pixel</li>
 * </ul>
 * </p>
 *
 * <p>
 * World files describing a map on the Universal Transverse Mercator coordinate system (UTM) use these conventions:
 * <ul>
 *     <li>D and B are usually 0, since the image pixels are usually made to align with the UTM grid</li>
 *     <li>C is the UTM easting</li>
 *     <li>F is the UTM northing</li>
 *     <li>Units are always meters per pixel</li>
 * </ul>
 * </p>
 */
public class TiffWorldFileFormat implements DataSource {

    private static final int PIXEL_SCALE_X = 0;
    private static final int PIXEL_SCALE_Y = 3;
    private static final int COORDINATE_X = 4;
    private static final int COORDINATE_Y = 5;

    @Override
    public List<WorldSection> getWorldSections() {
        return null;
    }
}
