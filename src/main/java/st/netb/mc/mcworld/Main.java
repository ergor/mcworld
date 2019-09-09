
package st.netb.mc.mcworld;


import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {

        File dir = new File(args[0]);
        List<File> tiffFiles = getAllTiffFiles(dir);


        BufferedImage bufferedImage;

        for (File file : tiffFiles) {
            try {
                bufferedImage = Imaging.getBufferedImage(file);
                int value = bufferedImage.getRGB(0, 0);
                int r = bufferedImage.getColorModel().getRed(value);
                int g = bufferedImage.getColorModel().getGreen(value);
                int b = bufferedImage.getColorModel().getBlue(value);
                int a = bufferedImage.getColorModel().getAlpha(value);
                int aa = bufferedImage.getColorModel().getAlpha(value);
            } catch (ImageReadException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static List<File> getAllTiffFiles(File dir) {
        return Arrays.stream(dir.listFiles())
                .filter(p -> p.isFile() && p.getName().endsWith(".tif"))
                .collect(Collectors.toList());
    }
}
