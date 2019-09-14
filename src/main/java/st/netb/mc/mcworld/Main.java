
package st.netb.mc.mcworld;

import com.sun.media.jai.codec.ByteArraySeekableStream;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.SeekableStream;

import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import com.sun.media.jai.codec.ImageDecoder;
import st.netb.mc.mcworld.datastructs.raw.SosiFile;
import st.netb.mc.mcworld.datastructs.raw.WorldSection;

public class Main {

    public static void main(String[] args) {

        File dir = new File("./data/dtm");
        //List<Path> tiffFiles = getAllTiffFiles(dir);
        List<SosiFile> sosiFiles = getAllSosiFiles(dir);
        List<WorldSection> worldSections = new ArrayList<>(1_000_000);

        for (SosiFile sosiFile : sosiFiles) {
            try {
                SeekableStream stream = new ByteArraySeekableStream(Files.readAllBytes(sosiFile.getImageFilePath()));
                String[] names = ImageCodec.getDecoderNames(stream);
                ImageDecoder dec = ImageCodec.createImageDecoder(names[0], stream, null);
                RenderedImage im = dec.decodeAsRenderedImage();
                Raster raster = im.getData();

                WorldSection worldSection = new WorldSection(
                        raster,
                        sosiFile.getNorthingMin(),
                        sosiFile.getNorthingMax(),
                        sosiFile.getEastingMin(),
                        sosiFile.getEastingMax());

                worldSections.add(worldSection);
                System.out.println(worldSections.size());
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
}
