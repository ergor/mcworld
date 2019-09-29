package st.netb.mc.mcworld.datasource;

import st.netb.mc.mcworld.datastructs.raw.WorldSection;
import st.netb.mc.mcworld.datastructs.raw.coordinates.CoordinateSystem;
import st.netb.mc.mcworld.datastructs.raw.coordinates.Coordinate;

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

public class SosiFormat implements DataSource {

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
                        () -> DataSource.readImage(file.imageFilePath),
                        file.resX,
                        file.resY,
                        file.coordinateSystem.transform(
                                new Coordinate(file.eastingMin, file.northingMin),
                                new Coordinate(file.eastingMax, file.northingMax)));
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

        private CoordinateSystem coordinateSystem = CoordinateSystem.UTM_NORTH;
        private double northingMin;
        private double eastingMin;
        private double northingMax;
        private double eastingMax;
        private double resX;
        private double resY;
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

            this.resX = Float.parseFloat(resolutionMatcher.group(1));
            this.resY = Float.parseFloat(resolutionMatcher.group(2));

            this.northingMin = fromFixed(coordsMatcher.group(1));
            this.eastingMin = fromFixed(coordsMatcher.group(2));
            this.northingMax = fromFixed(coordsMatcher.group(3));
            this.eastingMax = fromFixed(coordsMatcher.group(4));

            this.imageFilePath = Paths.get(file.getParentFile().getPath(), imageFileMatcher.group(1));
        }
    }
}
