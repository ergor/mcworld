package st.netb.mc.mcworld;

import org.bukkit.Bukkit;
import st.netb.mc.mcworld.datasource.DataSource;
import st.netb.mc.mcworld.datasource.FileType;
import st.netb.mc.mcworld.datastructs.minecraft.coordinates.ChunkLocation;
import st.netb.mc.mcworld.datastructs.raw.Tuple;
import st.netb.mc.mcworld.datastructs.raw.World;
import st.netb.mc.mcworld.datastructs.raw.WorldSection;
import st.netb.mc.mcworld.datastructs.raw.geolocation.Coordinate;
import st.netb.mc.mcworld.datastructs.raw.geolocation.CoordinateSystem;
import st.netb.mc.mcworld.datastructs.raw.geolocation.GeoArea;
import st.netb.mc.mcworld.datastructs.raw.geolocation.GeodeticDatum;
import st.netb.mc.mcworld.rendering.AnvilRenderer;
import st.netb.mc.mcworld.rendering.GifRenderer;
import st.netb.mc.mcworld.rendering.IntermediateOutput;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Alexander Sagen
 * @date 2021-03-05
 */
public class MainRenderer {

    private File temporaryDir;

    public void init(String source, boolean fastRender) {
        Bukkit.broadcastMessage("§6§l    DEBUG - 1");

        World world = getWorld(FileType.SOSI, new File(source).toPath());

        Bukkit.broadcastMessage("§6§l    DEBUG - 2");

        GeoArea testArea = new GeoArea(
                GeodeticDatum.EUREF89,
                CoordinateSystem.UTM32N,
                new Coordinate(597255, 6640726),
                new Coordinate(601000, 6643455));

        Bukkit.broadcastMessage("§6§l    DEBUG - 3");

        List<WorldSection> worldSections = world.getSections().stream()
        .filter(ws -> testArea.contains(ws.getArea()))
        .collect(Collectors.toList());

        Bukkit.broadcastMessage("§6§l    DEBUG - 4");

        Map<ChunkLocation, ChunkBuilder> incompleteChunks = new HashMap<>();

        Bukkit.broadcastMessage("§6§l    DEBUG - 5");

        temporaryDir = new File("tmp");

        Bukkit.broadcastMessage("§6§l    DEBUG - 6");

        if (!fastRender) {
            IntermediateOutput ioWriter = new IntermediateOutput(temporaryDir);

            Bukkit.broadcastMessage("§6§l    DEBUG - 7");

            for (WorldSection worldSection : worldSections) {
                Tuple<List<ChunkBuilder>> chunkBuilders =
                        WorldMapper.toChunkBuilders(world, worldSection, incompleteChunks);

                Bukkit.broadcastMessage("§6§l    DEBUG - 8");

                List<ChunkBuilder> completeChunks = chunkBuilders.first();
                List<ChunkBuilder> intersectingChunks = chunkBuilders.second();

                Bukkit.broadcastMessage("§6§l    DEBUG - 9");

                List<ChunkBuilder> updatedChunks = incompleteChunks.values().stream()
                        .filter(ChunkBuilder::isComplete)
                        .collect(Collectors.toList());

                Bukkit.broadcastMessage("§6§l    DEBUG - 10");

                updatedChunks.forEach(cs -> {
                    completeChunks.add(cs);
                    incompleteChunks.remove(cs.getChunkLocation());
                });

                Bukkit.broadcastMessage("§6§l    DEBUG - 11");

                intersectingChunks.forEach(cs ->
                        incompleteChunks.put(cs.getChunkLocation(), cs));

                Bukkit.broadcastMessage("§6§l    DEBUG - 12");

                ioWriter.writeFiles(completeChunks);

                Bukkit.broadcastMessage("§6§l    DEBUG - 13");

                System.out.println("rendered " + completeChunks.size() + " chunks, " + intersectingChunks.size() + " on hold");
            }
        }
        else {
            System.out.println("Warning: -f was specified. Will only re-render based on existing processed data");
        }
    }

    public void renderGif() {
        System.out.println("rendering GIF...");
        GifRenderer gif = new GifRenderer(temporaryDir, new File("out"));
        gif.render();
    }

    public void renderAnvil() {
        System.out.println("rendering minecraft world...");
        AnvilRenderer anvilRenderer = new AnvilRenderer(temporaryDir, new File("anvil/region"));
        anvilRenderer.render();
    }

    private World getWorld(FileType dataFileType, Path dataDirectory) {

        List<File> dataSourceFiles = Arrays.stream(dataDirectory.toFile().listFiles())
                .filter(p -> p.isFile() && p.getName().endsWith(dataFileType.getExtension()))
                .sorted()
                .collect(Collectors.toList());

        DataSource dataSource = DataSource.getInstance(dataFileType, dataSourceFiles);

        return new World(dataSource.getWorldSections());
    }

}
