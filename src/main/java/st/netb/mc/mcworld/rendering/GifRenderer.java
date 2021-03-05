package st.netb.mc.mcworld.rendering;

import org.bukkit.Bukkit;
import st.netb.mc.mcworld.Constants;
import st.netb.mc.mcworld.datastructs.minecraft.coordinates.RegionLocation;
import st.netb.mc.mcworld.datastructs.raw.RegionHeightmap;
import st.netb.mc.mcworld.datastructs.raw.Tuple;

import javax.imageio.ImageIO;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.BiFunction;

public class GifRenderer extends Renderer {

    private File intermediateDir;
    private File outputDir;

    private BufferedImage bufferedImage;
    private WritableRaster writableRaster;

    private static final int REGION_PIXEL_WIDTH = Constants.CHUNK_LEN_X * Constants.REGION_LEN_X;
    private static final int REGION_PIXEL_HEIGHT = Constants.CHUNK_LEN_Z * Constants.REGION_LEN_Z;

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

        Map<File, RegionLocation> regionLocationMap = mapToRegions(
                Arrays.asList(intermediateDir.listFiles()));

        Tuple<RegionLocation> bounds = getBounds(regionLocationMap);
        RegionLocation lowerBound = bounds.first();
        RegionLocation upperBound = bounds.second();

        {
            int width = REGION_PIXEL_WIDTH * (1 + (upperBound.getX() - lowerBound.getX()));
            int height = REGION_PIXEL_HEIGHT * (1 + (upperBound.getZ() - lowerBound.getZ()));

            bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

            writableRaster = Raster.createWritableRaster(
                    bufferedImage.getSampleModel(),
                    new Point(0, 0));
        }

        for (File file : regionLocationMap.keySet()) {
            try {
                RegionHeightmap region = new RegionHeightmap(
                        regionLocationMap.get(file),
                        Files.readAllBytes(file.toPath()));

                writeRegionToRaster(region, lowerBound);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        renderGif("overview.gif");
    }

    private void writeRegionToRaster(RegionHeightmap region,
                                     RegionLocation lowerBound) {

//        try {
            //File file = File.createTempFile("pixel_out_of_bounds", ".txt");

            //try(Writer writer = Channels.newWriter(new FileOutputStream(file.getAbsoluteFile(), true).getChannel(), "UTF-8")) {

                int xOffset = REGION_PIXEL_WIDTH * (region.getLocation().getX() - lowerBound.getX());
                int zOffset = REGION_PIXEL_HEIGHT * (region.getLocation().getZ() - lowerBound.getZ());

                int eCounter = 0;
                for (int z = 0; z < REGION_PIXEL_HEIGHT; z++) {
                    for (int x = 0; x < REGION_PIXEL_WIDTH; x++) {
                        try {
                            writableRaster.setPixel(
                                    xOffset + x,
                                    zOffset + z,
                                    new int[]{region.getHeight(x, z)});
                        } catch (Exception e) {
                            if(eCounter++ % 1000 == 0) {
                                Bukkit.broadcastMessage(eCounter + " pixels out of bounds....");
                            }
                            //writer.append("Pixel out of bounds: ").append(String.valueOf(xOffset + x)).append(" - ").append(String.valueOf(zOffset + z));
                        }
                    }
                }
                bufferedImage.setData(writableRaster);

                //writer.flush();
            //}

//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    private void renderGif(String fileName) {
        File outputFile = Paths.get(outputDir.getPath(), fileName).toFile();

        try {
            ImageIO.write(bufferedImage, "GIF", outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param locations The list of locations.
     * @param predicate Used in reducer. If it returns true, then the current element is set as result.
     *                  Otherwise, the result is unchanged.
     *
     * @return The location that meets the conditions of the predicate.
     */
    private RegionLocation getBound(Collection<RegionLocation> locations,
                                    BiFunction<RegionLocation, RegionLocation, Boolean> predicate) {
        return locations.stream()
                .reduce((result, element) -> predicate.apply(result, element) ? element : result)
                .orElseThrow(() -> new RuntimeException("Renderer: could not get bounds of locations"));
    }

    /**
     * Makes a tuple of (in order) the lower and upper bounds of the map
     */
    Tuple<RegionLocation> getBounds(Map<File, RegionLocation> regionLocationMap) {
        RegionLocation lowerBound = getBound(
                regionLocationMap.values(),
                (result, element) -> element.getX() < result.getX() || element.getZ() < result.getZ());

        RegionLocation upperBound = getBound(
                regionLocationMap.values(),
                (result, element) -> element.getX() > result.getX() || element.getZ() > result.getZ());

        return new Tuple<>(lowerBound, upperBound);
    }
}
