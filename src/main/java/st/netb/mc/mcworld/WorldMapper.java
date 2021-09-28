package st.netb.mc.mcworld;

import st.netb.mc.mcworld.datastructs.minecraft.Coord2D;
import st.netb.mc.mcworld.datastructs.raw.geolocation.Coordinate;
import st.netb.mc.mcworld.datastructs.raw.World;
import st.netb.mc.mcworld.datastructs.raw.Tuple;
import st.netb.mc.mcworld.datastructs.raw.WorldSection;

import java.awt.image.Raster;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WorldMapper {

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
            Map<Coord2D.Chunk, ChunkBuilder> incompleteChunks) {

        Coordinate worldOrigin = world.getArea().getOrigin();
        Coordinate sectionOrigin = worldSection.getArea().getOrigin();
        double resX = worldSection.getResX();
        double resY = worldSection.getResY();

        Raster raster = worldSection.getRaster();

        Map<Coord2D.Chunk, ChunkBuilder> chunkBuilderMap = new HashMap<>();

        for (int pixelY = 0; pixelY < raster.getHeight(); pixelY++) {
            for (int pixelX = 0; pixelX < raster.getWidth(); pixelX++) {

                /* equations derived from: https://en.wikipedia.org/wiki/World_file
                 *
                 * X_coord = X_distance_units_per_pixel * X_pixel + X_coord_top_left_corner
                 * (same for Y)
                 *
                 * X_pixel = (X_coord - X_coord_top_left_corner) / X_distance_units_per_pixel
                 * (same for Y)
                 */

                // Get the X and Y coordinates this pixel represents:
                double xCoord = (pixelX * resX) + sectionOrigin.x;
                double yCoord = (pixelY * resY) + sectionOrigin.y;

                // Now find what pixel that would be if we treat the whole world as one image:
                double worldPixelX = (xCoord - worldOrigin.x) / resX;
                double worldPixelY = (yCoord - worldOrigin.y) / resY;

                /* Now scale such that 1 pixel == 1 block.
                 * Because 1 block = 1x1 m, we need to scale by the distance_units_per_pixel factor
                 * again. It could be done all at once in the step above, but this way it's clearer
                 * what we are doing.
                 * Since we have already scaled by the factor above, we need to take the absolute value
                 * this time so we don't end up with negative positions.
                 */
                Coord2D.Relative<Coord2D.Block, Coord2D.Chunk> blockChunkRelative = Coord2D.blockInChunk(
                        new Coord2D.Block(
                                (int) (worldPixelX * Math.abs(resX)),
                                (int) (worldPixelY * Math.abs(resY))
                        )
                );

                Coord2D.Block blockLocation = blockChunkRelative.getRel();
                Coord2D.Chunk chunkLocation = blockChunkRelative.getAbs();

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
