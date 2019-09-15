
package st.netb.mc.mcworld;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import st.netb.mc.mcworld.datastructs.raw.ChunkSurface;
import st.netb.mc.mcworld.datastructs.raw.Tuple;
import st.netb.mc.mcworld.datastructs.raw.WorldSection;
import st.netb.mc.mcworld.formats.FileType;
import st.netb.mc.mcworld.formats.InputFormat;
import st.netb.mc.mcworld.formats.SosiFormat;


public class Main {

    public static void main(String[] args) {

        File dir = new File("data/dtm");

        InputFormat inputFormat = new SosiFormat(getInputFiles(dir, FileType.SOSI));
        List<WorldSection> worldSections = inputFormat.getWorldSections();

        Rectangle2D.Double globalArea = WorldMapper.getUsableArea(worldSections);

        Map<Point, ChunkSurface> incompleteChunks = new HashMap<>();

        IntermediateOutput ioWriter = new IntermediateOutput(new File("tmp"));

        for (WorldSection worldSection : worldSections) {

            Tuple<List<ChunkSurface>> chunks =
                    WorldMapper.mapToChunkSurfaces(globalArea, worldSection, incompleteChunks);

            List<ChunkSurface> completeChunks = chunks.first();
            List<ChunkSurface> intersectingChunks = chunks.second();

            List<ChunkSurface> updatedChunks = incompleteChunks.values().stream()
                    .filter(ChunkSurface::isComplete)
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
    }

    private static List<File> getInputFiles(File dir, FileType fileType) {
        return Arrays.stream(dir.listFiles())
                .filter(p -> p.isFile() && p.getName().endsWith(fileType.getExtension()))
                .sorted()
                .collect(Collectors.toList());
    }
}
