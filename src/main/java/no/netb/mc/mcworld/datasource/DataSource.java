package no.netb.mc.mcworld.datasource;

import com.sun.media.jai.codec.ByteArraySeekableStream;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageDecoder;
import com.sun.media.jai.codec.SeekableStream;
import no.netb.mc.mcworld.datastructs.raw.WorldSection;

import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;


public interface DataSource {

    List<WorldSection> getWorldSections();

    static DataSource getInstance(FileType fileType, List<File> dataSourceFiles) {
        return fileType.initializer.apply(dataSourceFiles);
    }

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
