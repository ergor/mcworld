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
import java.util.*;
import java.util.List;
import java.util.function.BiFunction;
import java.util.regex.Matcher;

public class GifRenderer implements Renderer {

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

        List<File> intermediateFiles = Arrays.asList(intermediateDir.listFiles());

        Map<File, RegionLocation> locationMap = locationMap(intermediateFiles);

        RegionLocation lowerBound = getBound(
                locationMap.values(),
                (result, element) -> element.x < result.x || element.z < result.z);

        RegionLocation upperBound = getBound(
                locationMap.values(),
                (result, element) -> element.x > result.x || element.z > result.z);
        {
            int width = REGION_PIXEL_WIDTH * (1 + (upperBound.x - lowerBound.x));
            int height = REGION_PIXEL_HEIGHT * (1 + (upperBound.z - lowerBound.z));

            bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

            writableRaster = Raster.createWritableRaster(
                    bufferedImage.getSampleModel(),
                    new Point(0, 0));
        }

        for (File file : locationMap.keySet()) {
            try {
                RegionHeightmap region = new RegionHeightmap(
                        locationMap.get(file),
                        Files.readAllBytes(file.toPath()));

                writeRegionToRaster(region, lowerBound);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        renderGif("output.gif");
    }

    private void writeRegionToRaster(RegionHeightmap region,
                                     RegionLocation lowerBound) {

        int xOffset = REGION_PIXEL_WIDTH * (region.getLocation().x - lowerBound.x);
        int zOffset = REGION_PIXEL_HEIGHT * (region.getLocation().z - lowerBound.z);

        for (int z = 0; z < REGION_PIXEL_HEIGHT; z++) {
            for (int x = 0; x < REGION_PIXEL_WIDTH; x++) {
                writableRaster.setPixel(
                        xOffset + x,
                        zOffset + z,
                        new int[] { region.getHeight(x, z) });
            }
        }

        bufferedImage.setData(writableRaster);
    }

    private void renderGif(String fileName) {
        File outputFile = Paths.get(outputDir.getPath(), fileName).toFile();

        try {
            ImageIO.write(bufferedImage, "GIF", outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map<File, RegionLocation> locationMap(List<File> intermediateFiles) {

        Map<File, RegionLocation> map = new HashMap<>();

        for (File file : intermediateFiles) {

            Matcher matcher = IntermediateOutput.tempFilePattern.matcher(file.getName());
            if (matcher.find()) {
                RegionLocation regionLocation = new RegionLocation(
                        Integer.parseInt(matcher.group(2)),
                        Integer.parseInt(matcher.group(1)));

                map.put(file, regionLocation);
            }
            else {
                System.out.println(file.getName() + ": illegal file name");
            }
        }

        return map;
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
                .orElseThrow(() -> new RuntimeException("GifRenderer: could not get bounds of locations"));
    }
}
