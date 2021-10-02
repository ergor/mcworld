package no.netb.mc.mcworld.datasource;

import no.netb.mc.mcworld.datastructs.raw.WorldSection;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.nio.file.Path;
import java.util.List;


public interface DataSource {

    List<WorldSection> getWorldSections();

    static DataSource getInstance(FileType fileType, List<File> dataSourceFiles) {
        return fileType.initializer.apply(dataSourceFiles);
    }

    static Raster readImage(Path filePath) {
        try {
            BufferedImage bufferedImage = ImageIO.read(filePath.toFile());
            return bufferedImage.getData();
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to read image " + filePath.toString() + ":", e);
        }
    }
}
