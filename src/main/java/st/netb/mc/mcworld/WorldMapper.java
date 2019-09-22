package st.netb.mc.mcworld;

import st.netb.mc.mcworld.datastructs.minecraft.coordinates.BlockLocation;
import st.netb.mc.mcworld.datastructs.minecraft.coordinates.ChunkLocation;
import st.netb.mc.mcworld.datastructs.minecraft.coordinates.MinecraftLocation;
import st.netb.mc.mcworld.datastructs.minecraft.coordinates.referenceframe.ReferenceFrame;
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

                Tuple<MinecraftLocation> locationTuple = new BlockLocation(
                        (int)(x + localGrid.x),
                        (int)(y + localGrid.y))
                        .tryReferencedTo(ReferenceFrame.CHUNK);

                BlockLocation blockLocation = (BlockLocation) locationTuple.first();
                ChunkLocation chunkLocation = (ChunkLocation) locationTuple.second();

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
