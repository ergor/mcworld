package st.netb.mc.mcworld.formats;

import st.netb.mc.mcworld.datastructs.raw.WorldSection;

import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SosiFormat implements InputFormat {

    private List<SosiFile> files;
    private List<WorldSection> worldSections;

    private static final Pattern resolutionPattern = Pattern.compile("\\.\\.\\.PIXEL-STØRR\\s+(\\d+\\.?\\d*)\\s(\\d+\\.?\\d*)");
    private static final Pattern coordsPattern = Pattern.compile("\\.\\.NØ\\s+(\\d+)\\s(\\d+)\\s+(\\d+)\\s(\\d+)");
    private static final Pattern imageFilePattern = Pattern.compile("\\.\\.\\.BILDE-FIL \"(.+)\"");

    public SosiFormat(List<File> files) {
        this.files = files.stream()
                .map(SosiFile::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<WorldSection> getWorldSections() {

        if (worldSections == null) {
            worldSections = new ArrayList<>(files.size());
            for (SosiFile file : files) {
                WorldSection worldSection = new WorldSection(
                        () -> InputFormat.readImage(file.imageFilePath),
                        file.resolution,
                        file.northingMin,
                        file.northingMax,
                        file.eastingMin,
                        file.eastingMax);
                worldSections.add(worldSection);
            }
        }

        return worldSections;
    }

    private static double fromFixed(String fixedPoint) {
        String first = fixedPoint.substring(0, fixedPoint.length()-2);
        String second = fixedPoint.substring(fixedPoint.length() - 2);

        return Double.parseDouble(first + "." + second);
    }

    private static class SosiFile {

        private double northingMin;
        private double eastingMin;
        private double northingMax;
        private double eastingMax;
        private double resolution;
        private Path imageFilePath;

        SosiFile(File file) {

            String contents;
            try {
                contents = new String(Files.readAllBytes(file.toPath()));
            } catch (IOException e) {
                throw new RuntimeException("could not create SOSI file object: read threw an exception:", e);
            }

            Matcher resolutionMatcher = resolutionPattern.matcher(contents);
            Matcher coordsMatcher = coordsPattern.matcher(contents);
            Matcher imageFileMatcher = imageFilePattern.matcher(contents);

            resolutionMatcher.find();
            coordsMatcher.find();
            imageFileMatcher.find();

            {
                float xRes = Float.parseFloat(resolutionMatcher.group(1));
                float yRes = Float.parseFloat(resolutionMatcher.group(2));

                if (xRes != yRes) {
                    throw new RuntimeException("x and y resolution must be same");
                }

                this.resolution = xRes;
            }

            this.northingMin = fromFixed(coordsMatcher.group(1));
            this.eastingMin = fromFixed(coordsMatcher.group(2));
            this.northingMax = fromFixed(coordsMatcher.group(3));
            this.eastingMax = fromFixed(coordsMatcher.group(4));

            this.imageFilePath = Paths.get(file.getParentFile().getPath(), imageFileMatcher.group(1));
        }
    }
}
