package st.netb.mc.mcworld;

import st.netb.mc.mcworld.datastructs.minecraft.coordinates.BlockLocation;
import st.netb.mc.mcworld.datastructs.minecraft.coordinates.ChunkLocation;
import st.netb.mc.mcworld.datastructs.minecraft.coordinates.MinecraftLocation;
import st.netb.mc.mcworld.datastructs.raw.coordinates.WorldGrid;
import st.netb.mc.mcworld.datastructs.raw.World;
import st.netb.mc.mcworld.datastructs.raw.coordinates.GeoArea;
import st.netb.mc.mcworld.datastructs.raw.Tuple;
import st.netb.mc.mcworld.datastructs.raw.WorldSection;

import java.awt.image.Raster;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WorldMapper {

//    public static GeoArea getWorldArea(List<WorldSection> worldSections) {
//        double globalNorthingMin = Double.MAX_VALUE;
//        double globalEastingMin = Double.MAX_VALUE;
//        double globalNorthingMax = Double.MIN_VALUE;
//        double globalEastingMax = Double.MIN_VALUE;
//
//        for (WorldSection worldSection : worldSections) {
//            GeoArea localArea = worldSection.getArea();
//
//            double northingMin = localArea.getMinY();
//            double northingMax = localArea.getMaxY();
//            double eastingMin = localArea.getMinX();
//            double eastingMax = localArea.getMaxX();
//
//            if (northingMin < globalNorthingMin) {
//                globalNorthingMin = northingMin;
//            }
//            if (northingMax > globalNorthingMax) {
//                globalNorthingMax = northingMax;
//            }
//            if (eastingMin < globalEastingMin) {
//                globalEastingMin = eastingMin;
//            }
//            if (eastingMax > globalEastingMax) {
//                globalEastingMax = eastingMax;
//            }
//        }
//
//        double width = globalEastingMax - globalEastingMin;
//        double height = globalNorthingMax - globalNorthingMin;
//
//        return new GeoArea(globalEastingMin, globalNorthingMin, width, height);
//    }
//
//    /**
//     * Return an area that is evenly disivible by chunk size, ie 16.
//     */
//    public static GeoArea getUsableArea(GeoArea rawArea) {
//
//        double x = Math.ceil(rawArea.x);
//        double y = Math.ceil(rawArea.y);
//
//        double xCropLength = x - rawArea.x;
//        double yCropLength = y - rawArea.y;
//
//        int chunkCountEasting = (int) (rawArea.width - xCropLength) / 16;
//        int chunkCountNorthing = (int) (rawArea.height - yCropLength) / 16;
//
//        return new GeoArea(x, y, chunkCountEasting * 16.0, chunkCountNorthing * 16.0);
//    }
//
//    /**
//     * Returns a rectangle of same size as input, where its origin is zero,
//     * ie. (x_origin, y_origin) == (0,0)
//     * @param area
//     * @return
//     */
//    public static GeoArea normalizeArea(GeoArea area) {
//        return new GeoArea(0, 0, area.width, area.height);
//    }
//
//    /**
//     * Takes a subsection of a reference area whose origin is not in (0, 0),
//     * and normalizes it within that reference.
//     *
//     * @param referenceArea
//     * @param localArea
//     * @return
//     */
//    public static GeoArea normalizeArea(
//            GeoArea referenceArea,
//            GeoArea localArea) {
//        return new GeoArea(
//                localArea.x - referenceArea.x,
//                localArea.y - referenceArea.y,
//                localArea.width,
//                localArea.height);
//    }

//    /**
//     * Maps the pixel in a world section to the chunk it belongs to and the location in the chunk
//     * @return A (chunk_coordinates, block_coordinates) tuple.
//     */
//    private static Tuple<Point> toChunkLocation(GeoArea globalArea, WorldSection worldSection, Point pixel) {
//
//        double resolution = worldSection.getResolution();
//        GeoArea localArea = worldSection.getArea();
//
//        double distanceXToOrigo = localArea.x - globalArea.x;
//        double distanceYToOrigo = localArea.y - globalArea.y;
//
//        Point2D offset = new Point2D.Double(pixel.x * resolution, pixel.y * resolution);
//
//        double x = distanceXToOrigo + offset.getX();
//        double y = distanceYToOrigo + offset.getY();
//
//        int chunkX = (int)x / 16;
//        int chunkY = (int)y / 16;
//
//        int blockX = (int)x % 16;
//        int blockY = (int)y % 16;
//
//        return new Tuple<>(
//                new Point(chunkX, chunkY),
//                new Point(blockX, blockY));
//    }
//
    /**
     * @param globalArea (in param): the entire area of the world
     * @param worldSection (in param): the worldsection
     * @param output (out param): the chunks that are entirely inside this world section
     * @param incompleteChunks (in/out param): chunks that are _not_ entirely inside this world section
     *                     and thus are incomplete
     * @return whether there are intersecting surface chunks
     */
    public static Tuple<List<ChunkBuilder>> toChunkBuilders(
            World world,
            WorldSection worldSection,
            Map<ChunkLocation, ChunkBuilder> incompleteChunks) {

        WorldGrid localGrid = GeoArea.toWorldGrid(world.getArea(), worldSection.getArea());

        Raster raster = worldSection.getRaster();

        Map<ChunkLocation, ChunkBuilder> chunkBuilderMap = new HashMap<>();

        for (int pixelY = 0; pixelY < raster.getHeight(); pixelY++) {
            for (int pixelX = 0; pixelX < raster.getWidth(); pixelX++) {
                //Point pixel = new Point(x, y);

                //Tuple<Point> tuple = toChunkLocation(globalArea, worldSection, pixel);

                double x = (double) pixelX * worldSection.getResolution();
                double y = (double) pixelY * worldSection.getResolution();

                Tuple<MinecraftLocation> tuple = new BlockLocation(
                        (int)(x + localGrid.x),
                        (int)(y + localGrid.y))
                        .referencedToChunk();

                BlockLocation blockLocation = (BlockLocation) tuple.first();
                ChunkLocation chunkLocation = (ChunkLocation) tuple.second();

                float height = raster.getPixel(pixelX, pixelY, (float[]) null)[0];

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
