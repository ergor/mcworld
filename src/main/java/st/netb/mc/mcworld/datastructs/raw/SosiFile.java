package st.netb.mc.mcworld.datastructs.raw;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SosiFile {

    private double northingMin;
    private double eastingMin;
    private double northingMax;
    private double eastingMax;
    private Path imageFilePath;

    private static final Pattern coordsPattern = Pattern.compile("\\.\\.NØ\\s+(\\d+)\\s(\\d+)\\s+(\\d+)\\s(\\d+)");
    private static final Pattern imageFilePattern = Pattern.compile("\\.\\.\\.BILDE-FIL \"(.+)\"");

    public SosiFile(File file) {
        if (!file.exists()) {
            throw new RuntimeException("could not create SOSI file object: file does not exists");
        }
        if (!file.canRead()) {
            throw new RuntimeException("could not create SOSI file object: missing read permissions");
        }

        String contents = null;
        try {
            contents = new String(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            throw new RuntimeException("could not create SOSI file object: read threw an exception:", e);
        }
        Matcher coordsMatcher = coordsPattern.matcher(contents);
        Matcher imageFileMatcher = imageFilePattern.matcher(contents);

        boolean cm = coordsMatcher.find();
        boolean im = imageFileMatcher.find();

        this.northingMin = fromFixed(coordsMatcher.group(1));
        this.eastingMin = fromFixed(coordsMatcher.group(2));
        this.northingMax = fromFixed(coordsMatcher.group(3));
        this.eastingMax = fromFixed(coordsMatcher.group(4));

        this.imageFilePath = Paths.get(file.getParentFile().getPath(), imageFileMatcher.group(1));
    }

    private double fromFixed(String fixedPoint) {
        String first = fixedPoint.substring(0, fixedPoint.length()-2);
        String second = fixedPoint.substring(fixedPoint.length() - 2);

        return Double.parseDouble(first + "." + second);
    }

    public double getNorthingMin() {
        return northingMin;
    }

    public double getEastingMin() {
        return eastingMin;
    }

    public double getNorthingMax() {
        return northingMax;
    }

    public double getEastingMax() {
        return eastingMax;
    }

    public Path getImageFilePath() {
        return imageFilePath;
    }
}
