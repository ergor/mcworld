package st.netb.mc.mcworld.formats;

import com.sun.media.jai.codec.ByteArraySeekableStream;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageDecoder;
import com.sun.media.jai.codec.SeekableStream;
import st.netb.mc.mcworld.datastructs.raw.WorldSection;

import java.awt.geom.Rectangle2D;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;


public interface InputFormat {

    FileType getFileType();
    Rectangle2D.Double getWorldArea();
    List<WorldSection> getWorldSections();

    static Raster readImage(Path filePath) {
        try {
            SeekableStream stream = new ByteArraySeekableStream(Files.readAllBytes(filePath));
            String[] names = ImageCodec.getDecoderNames(stream);
            ImageDecoder dec = ImageCodec.createImageDecoder(names[0], stream, null);
            RenderedImage im = dec.decodeAsRenderedImage();
            return im.getData(); // takes relatively long time (~1s for large files)
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to read image " + filePath.toString() + ":", e);
        }
    }
}
