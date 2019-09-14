
package st.netb.mc.mcworld;

import com.sun.media.jai.codec.ByteArraySeekableStream;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.SeekableStream;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import com.sun.media.jai.codec.ImageDecoder;
import st.netb.mc.mcworld.datastructs.raw.SosiFile;
import st.netb.mc.mcworld.datastructs.raw.ChunkSurface;
import st.netb.mc.mcworld.datastructs.raw.WorldSection;


public class Main {

    public static void main(String[] args) {

        File dir = new File("./data/dtm");
        //List<Path> tiffFiles = getAllTiffFiles(dir);
        List<SosiFile> sosiFiles = getAllSosiFiles(dir);
        List<WorldSection> worldSections = new ArrayList<>(1_000_000);

        Rectangle2D.Double worldArea = getWorldArea(sosiFiles);
        Rectangle2D.Double usableArea = WorldMapper.getUsableArea(worldArea);

        Map<Point, ChunkSurface> incompleteChunks = new HashMap<>();

        for (SosiFile sosiFile : sosiFiles) {
            System.out.println(sosiFile.getImageFilePath());
            try {
                SeekableStream stream = new ByteArraySeekableStream(Files.readAllBytes(sosiFile.getImageFilePath()));
                String[] names = ImageCodec.getDecoderNames(stream);
                ImageDecoder dec = ImageCodec.createImageDecoder(names[0], stream, null);
                RenderedImage im = dec.decodeAsRenderedImage();

                Raster raster = im.getData(); // takes relatively long time (~1s for large files)

                WorldSection worldSection = new WorldSection(
                        raster,
                        sosiFile.getNorthingMin(),
                        sosiFile.getNorthingMax(),
                        sosiFile.getEastingMin(),
                        sosiFile.getEastingMax());

                List<ChunkSurface> output = new ArrayList<>(500);
                List<ChunkSurface> intersecting = new ArrayList<>(50);

                WorldMapper.mapToChunkSurfaces(
                        usableArea, worldSection, incompleteChunks, output, intersecting);

                List<ChunkSurface> updated = incompleteChunks.values().stream()
                        .filter(ChunkSurface::isComplete)
                        .collect(Collectors.toList());

                updated.forEach(cs -> {
                    output.add(cs);
                    incompleteChunks.remove(cs.getChunkLocation());
                });

                intersecting.forEach(cs -> incompleteChunks.put(cs.getChunkLocation(), cs));

                System.out.println(output.size());
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println(worldSections.size());
    }

    private static List<SosiFile> getAllSosiFiles(File dir) {
        return Arrays.stream(dir.listFiles())
                .filter(p -> p.isFile() && p.getName().endsWith(".sos"))
                .map(SosiFile::new)
                .collect(Collectors.toList());
    }

    private static Rectangle2D.Double getWorldArea(List<SosiFile> sosiFiles) {
        double globalNorthingMin = Double.MAX_VALUE;
        double globalEastingMin = Double.MAX_VALUE;
        double globalNorthingMax = Double.MIN_VALUE;
        double globalEastingMax = Double.MIN_VALUE;

        for (SosiFile sosiFile : sosiFiles) {
            double northingMin = sosiFile.getNorthingMin();
            double eastingMin = sosiFile.getEastingMin();
            double northingMax = sosiFile.getNorthingMax();
            double eastingMax = sosiFile.getEastingMax();

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
}
