package st.netb.mc.mcworld;

import st.netb.mc.mcworld.datastructs.raw.Tuple;
import st.netb.mc.mcworld.datastructs.raw.WorldSection;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.Raster;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WorldMapper {

    public static Rectangle2D.Double getWorldArea(List<WorldSection> worldSections) {
        double globalNorthingMin = Double.MAX_VALUE;
        double globalEastingMin = Double.MAX_VALUE;
        double globalNorthingMax = Double.MIN_VALUE;
        double globalEastingMax = Double.MIN_VALUE;

        for (WorldSection worldSection : worldSections) {
            Rectangle2D.Double localArea = worldSection.getArea();

            double northingMin = localArea.getMinY();
            double northingMax = localArea.getMaxY();
            double eastingMin = localArea.getMinX();
            double eastingMax = localArea.getMaxX();

            if (northingMin < globalNorthingMin) {
                globalNorthingMin = northingMin;
            }
            if (northingMax > globalNorthingMax) {
                globalNorthingMax = northingMax;
            }
            if (eastingMin < globalEastingMin) {
                globalEastingMin = eastingMin;
            }
            if (eastingMax > globalEastingMax) {
                globalEastingMax = eastingMax;
            }
        }

        double width = globalEastingMax - globalEastingMin;
        double height = globalNorthingMax - globalNorthingMin;

        return new Rectangle2D.Double(globalEastingMin, globalNorthingMin, width, height);
    }

    /**
     * Return an area that is evenly disivible by chunk size, ie 16.
     */
    public static Rectangle2D.Double getUsableArea(Rectangle2D.Double rawArea) {

        double x = Math.ceil(rawArea.x);
        double y = Math.ceil(rawArea.y);

        double xCropLength = x - rawArea.x;
        double yCropLength = y - rawArea.y;

        int chunkCountEasting = (int) (rawArea.width - xCropLength) / 16;
        int chunkCountNorthing = (int) (rawArea.height - yCropLength) / 16;

        return new Rectangle2D.Double(x, y, chunkCountEasting * 16.0, chunkCountNorthing * 16.0);
    }

    /**
     * Maps the pixel in a world section to the chunk it belongs to and the location in the chunk
     * @return A (chunk_coordinates, block_coordinates) tuple.
     */
    private static Tuple<Point> mapToChunkLocation(Rectangle2D.Double globalArea, WorldSection worldSection, Point pixel) {

        double resolution = worldSection.getResolution();
        Rectangle2D.Double localArea = worldSection.getArea();

        double distanceXToOrigo = localArea.x - globalArea.x;
        double distanceYToOrigo = localArea.y - globalArea.y;

        Point2D offset = new Point2D.Double(pixel.x * resolution, pixel.y * resolution);

        double x = distanceXToOrigo + offset.getX();
        double y = distanceYToOrigo + offset.getY();

        int chunkX = (int)x / 16;
        int chunkY = (int)y / 16;

        int blockX = (int)x % 16;
        int blockY = (int)y % 16;

        return new Tuple<>(
                new Point(chunkX, chunkY),
                new Point(blockX, blockY));
    }

    /**
     * @param globalArea (in param): the entire area of the world
     * @param worldSection (in param): the worldsection
     * @param output (out param): the chunks that are entirely inside this world section
     * @param incompleteChunks (in/out param): chunks that are _not_ entirely inside this world section
     *                     and thus are incomplete
     * @return whether there are intersecting surface chunks
     */
    public static Tuple<List<ChunkBuilder>> mapToChunkSurfaces(
            Rectangle2D.Double globalArea,
            WorldSection worldSection,
            Map<Point, ChunkBuilder> incompleteChunks) {

        Raster raster = worldSection.getRaster();

        Map<Point, ChunkBuilder> chunkBuilderMap = new HashMap<>();

        for (int y = 0; y < raster.getHeight(); y++) {
            for (int x = 0; x < raster.getWidth(); x++) {
                Point pixel = new Point(x, y);

                Tuple<Point> tuple = mapToChunkLocation(globalArea, worldSection, pixel);
                Point chunkLocation = tuple.first();
                Point blockLocation = tuple.second();

                float height = raster.getPixel(x, y, (float[]) null)[0];

                ChunkBuilder chunkBuilder;
                if (incompleteChunks.containsKey(chunkLocation)) {
                    chunkBuilder = incompleteChunks.get(chunkLocation);
                }
                else {
                    chunkBuilder = chunkBuilderMap
                            .computeIfAbsent(chunkLocation, ChunkBuilder::new);
                }

                chunkBuilder.insert(blockLocation, height);
            }
        }

        return new Tuple<>(
                chunkBuilderMap.values().stream()
                        .filter(ChunkBuilder::isComplete)
                        .collect(Collectors.toList()),
                chunkBuilderMap.values().stream()
                        .filter(ChunkBuilder::isIncomplete)
                        .collect(Collectors.toList())
        );
    }
}
