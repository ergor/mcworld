
package st.netb.mc.mcworld;

import com.sun.media.jai.codec.ByteArraySeekableStream;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.SeekableStream;

import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import com.sun.media.jai.codec.ImageDecoder;
import st.netb.mc.mcworld.datastructs.Block;
import st.netb.mc.mcworld.datastructs.WorldSection;

public class Main {

    public static void main(String[] args) {

        File dir = new File(args[0]);
        List<Path> tiffFiles = getAllTiffFiles(dir);

        for (Path file : tiffFiles) {
            try {
                SeekableStream stream = new ByteArraySeekableStream(Files.readAllBytes(file));
                String[] names = ImageCodec.getDecoderNames(stream);
                ImageDecoder dec = ImageCodec.createImageDecoder(names[0], stream, null);
                RenderedImage im = dec.decodeAsRenderedImage();
                Raster raster = im.getData();
                //float[] value = new float[1];
                //raster.getPixel(0, 0, value); // returns height in meters
                //System.out.println(value[0]);

                List<Block> surface = RasterMapper.heightMapMapper(raster);
                //WorldSection worldSection = new WorldSection(raster.getWidth(), raster.getHeight());
                //surface.forEach(worldSection::insertBlock);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static List<Path> getAllTiffFiles(File dir) {
        return Arrays.stream(dir.listFiles())
                .filter(p -> p.isFile() && p.getName().endsWith(".tif"))
                .map(File::toPath)
                .collect(Collectors.toList());
    }
}
