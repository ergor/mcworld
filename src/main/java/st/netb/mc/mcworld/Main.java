
package st.netb.mc.mcworld;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import st.netb.mc.mcworld.datastructs.raw.GeoArea;
import st.netb.mc.mcworld.datastructs.raw.Tuple;
import st.netb.mc.mcworld.datastructs.raw.UTM;
import st.netb.mc.mcworld.datastructs.raw.WorldSection;
import st.netb.mc.mcworld.datasource.FileType;
import st.netb.mc.mcworld.datasource.DataSource;
import st.netb.mc.mcworld.rendering.IntermediateOutput;


public class Main {

    private static List<WorldSection> getWorldSections(FileType dataFileType,
                                                       Path dataDirectory,
                                                       Optional<GeoArea> geoArea) {

        List<File> dataSourceFiles = getInputFiles(dataFileType, dataDirectory);
        DataSource dataSource = DataSource.getInstance(dataFileType, dataSourceFiles);

        List<WorldSection> worldSections = dataSource.getWorldSections();

        return geoArea.map(area -> worldSections.stream()
                .filter(ws -> area.contains(ws.getArea()))
                .collect(Collectors.toList())
        ).orElse(worldSections);
    }

    public static void main(String[] args) {

        // 0, 0 -> 406399Ø 6434580N
        // x, y -> 410102Ø 6430815N
        //GeoArea globalArea = WorldMapper.getWorldArea(worldSections);
        UTM minCoords = new UTM(6430815, 406399);
        UTM maxCoords = new UTM(6434580, 410102);
        GeoArea testArea = new GeoArea(minCoords, maxCoords);

        GeoArea worldArea = WorldMapper.getUsableArea(testArea);

        List<WorldSection> worldSections = getWorldSections(
                FileType.SOSI,
                Paths.get("data", "dtm"),
                Optional.of(worldArea)
        ).stream()
                .map(w -> w.mapArea(WorldMapper.normalizeArea(worldArea, w.getArea())))
                .collect(Collectors.toList());

        GeoArea normalizedWorldArea = WorldMapper.normalizeArea(worldArea);

        Map<Point, ChunkBuilder> incompleteChunks = new HashMap<>();

        IntermediateOutput ioWriter = new IntermediateOutput(new File("tmp"));

        for (WorldSection worldSection : worldSections) {

            Tuple<List<ChunkBuilder>> chunkBuilders =
                    WorldMapper.toChunkBuilders(normalizedWorldArea, worldSection, incompleteChunks);

            List<ChunkBuilder> completeChunks = chunkBuilders.first();
            List<ChunkBuilder> intersectingChunks = chunkBuilders.second();

            List<ChunkBuilder> updatedChunks = incompleteChunks.values().stream()
                    .filter(ChunkBuilder::isComplete)
                    .collect(Collectors.toList());

            updatedChunks.forEach(cs -> {
                completeChunks.add(cs);
                incompleteChunks.remove(cs.getChunkLocation());
            });

            intersectingChunks.forEach(cs ->
                    incompleteChunks.put(cs.getChunkLocation(), cs));

            ioWriter.writeFiles(completeChunks);

            System.out.println("rendered " + completeChunks.size() + " chunks");
        }

        System.out.println("done");
    }

    private static List<File> getInputFiles(FileType fileType, Path dir) {
        return Arrays.stream(dir.toFile().listFiles())
                .filter(p -> p.isFile() && p.getName().endsWith(fileType.getExtension()))
                .sorted()
                .collect(Collectors.toList());
    }
}
