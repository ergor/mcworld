package st.netb.mc.mcworld.datastructs.raw;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SosiFile {

    private float northingMin;
    private float eastingMin;
    private float northingMax;
    private float eastingMax;
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

    private float fromFixed(String fixedPoint) {
        String first = fixedPoint.substring(0, fixedPoint.length()-2);
        String second = fixedPoint.substring(fixedPoint.length() - 2);

        return Float.parseFloat(first + "." + second);
    }

    public float getNorthingMin() {
        return northingMin;
    }

    public float getEastingMin() {
        return eastingMin;
    }

    public float getNorthingMax() {
        return northingMax;
    }

    public float getEastingMax() {
        return eastingMax;
    }

    public Path getImageFilePath() {
        return imageFilePath;
    }
}
