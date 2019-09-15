
package st.netb.mc.mcworld;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import st.netb.mc.mcworld.datastructs.raw.Tuple;
import st.netb.mc.mcworld.datastructs.raw.WorldSection;
import st.netb.mc.mcworld.formats.FileType;
import st.netb.mc.mcworld.formats.InputFormat;
import st.netb.mc.mcworld.formats.SosiFormat;
import st.netb.mc.mcworld.rendering.IntermediateOutput;


public class Main {

    public static void main(String[] args) {

        Path dir = Paths.get("data", "dtm");

        InputFormat inputFormat = new SosiFormat(getInputFiles(dir, FileType.SOSI));

        List<WorldSection> worldSections = inputFormat.getWorldSections();

        // 0, 0 -> 406399Ø 6434580N
        // x, y -> 410102Ø 6430815N
        Rectangle2D.Double globalArea = WorldMapper.getWorldArea(worldSections);
        Rectangle2D.Double testArea = new Rectangle2D.Double(
                406399,
                6430815,
                410102 - 406399,
                6434580 - 6430815
        );
        Rectangle2D.Double workingArea = WorldMapper.getUsableArea(testArea);
        worldSections = worldSections.stream()
                .filter(ws -> workingArea.contains(ws.getArea()))
                .collect(Collectors.toList());

        Map<Point, ChunkBuilder> incompleteChunks = new HashMap<>();

        IntermediateOutput ioWriter = new IntermediateOutput(new File("tmp"));

        for (WorldSection worldSection : worldSections) {

            Tuple<List<ChunkBuilder>> chunks =
                    WorldMapper.mapToChunkSurfaces(workingArea, worldSection, incompleteChunks);

            List<ChunkBuilder> completeChunks = chunks.first();
            List<ChunkBuilder> intersectingChunks = chunks.second();

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

    private static List<File> getInputFiles(Path dir, FileType fileType) {
        return Arrays.stream(dir.toFile().listFiles())
                .filter(p -> p.isFile() && p.getName().endsWith(fileType.getExtension()))
                .sorted()
                .collect(Collectors.toList());
    }
}
