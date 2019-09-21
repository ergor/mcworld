package st.netb.mc.mcworld.rendering;

import st.netb.mc.mcworld.Constants;
import st.netb.mc.mcworld.coordinates.RegionLocation;
import st.netb.mc.mcworld.datastructs.raw.RegionHeightmap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;

public class GifRenderer implements Renderer {

    private File intermediateDir;
    private File outputDir;

    public GifRenderer(File intermediateDir, File outputDir) {
        this.intermediateDir = intermediateDir;
        this.outputDir = outputDir;
    }

    @Override
    public void render() {

        if (!outputDir.exists()) {
            if (!outputDir.mkdir()) {
                throw new RuntimeException("could not create output directory");
            }
        }

        for (File file : intermediateDir.listFiles()) {

            Matcher matcher = IntermediateOutput.tempFilePattern.matcher(file.getName());
            if (matcher.find()) {
                try {
                    RegionLocation regionLocation = new RegionLocation(
                            Integer.parseInt(matcher.group(2)),
                            Integer.parseInt(matcher.group(1)));

                    RegionHeightmap region = new RegionHeightmap(regionLocation, Files.readAllBytes(file.toPath()));
                    renderGif(region, file.getName());
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                System.out.println(file.getName() + ": illegal file name");
            }
        }
    }

    private void renderGif(RegionHeightmap region, String fileName) {

        int width = Constants.CHUNK_LEN_X * Constants.REGION_LEN_X;
        int height = Constants.CHUNK_LEN_Z * Constants.REGION_LEN_Z;

        BufferedImage bufferedImage = new BufferedImage(
                width,
                height,
                BufferedImage.TYPE_BYTE_GRAY);

        WritableRaster writableRaster = Raster.createWritableRaster(
                bufferedImage.getSampleModel(),
                new Point(0, 0));

        for (int z = 0; z < height; z++) {
            for (int x = 0; x < width; x++) {
                writableRaster.setPixel(x, z, new int[] {region.getHeight(x, z)});
            }
        }

        bufferedImage.setData(writableRaster);

        File outputFile = Paths.get(outputDir.getPath(), fileName + ".gif").toFile();

        try {
            ImageIO.write(bufferedImage, "GIF", outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
