package st.netb.mc.mcworld.rendering;

import st.netb.mc.mcworld.Constants;
import st.netb.mc.mcworld.ChunkBuilder;

import java.awt.*;
import java.io.*;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Writes the height of each block at surface position (x, y)
 * as a single byte in an intermediate file.
 * Since a chunk has 16x16 blocks, the file shall be 256 bytes long.
 * <br><br>
 * Byte0 corresponds to (0, 0), and byte255 to (15, 15).
 * The bytes are saved line by line, ie. the first 16 bytes
 * correspond to (0..15, 0), the next 16 bytes (0..15, 1) etc.
 * <br><br>
 * The output filename shall follow the pattern of y-x.
 * For example, the chunk at (0, 5) will get the name "5-0"
 */
public class IntermediateOutput {

    private static final Pattern tempFilePattern = Pattern.compile("\\d+-\\d+");
    private File outputDir;

    public IntermediateOutput(File outputDir) {
        this.outputDir = outputDir;

        if (!outputDir.exists()) {
            if (!outputDir.mkdir()) {
                throw new RuntimeException("could not create temporary output directory");
            }
        }

        for (File file : outputDir.listFiles()) {
            if (tempFilePattern.matcher(file.getName()).matches()) {
                file.delete();
            }
        }
    }

    public void writeFiles(List<ChunkBuilder> chunkBuilders) {

        for (ChunkBuilder chunkBuilder : chunkBuilders) {

            byte[] chunkData = new byte[Constants.CHUNK_LEN_X * Constants.CHUNK_LEN_Y];
            Point location = chunkBuilder.getChunkLocation();

            try {
                float[][] heightMap = chunkBuilder.build();
                for (int y = 0; y < Constants.CHUNK_LEN_Y; y++) {
                    for (int x = 0; x < Constants.CHUNK_LEN_X; x++) {
                        chunkData[y * Constants.CHUNK_LEN_Y + x] = (byte) Math.ceil(heightMap[y][x]);
                    }
                }
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }

            try {
                String fileName;
                {
                    int regionX = location.x / Constants.REGION_LEN_X;
                    int regionY = location.y / Constants.REGION_LEN_Y;
                    fileName = regionY + "-" + regionX;
                }

                File outputFile = new File(Paths.get(outputDir.getPath(), fileName).toString());

                RandomAccessFile raf = new RandomAccessFile(outputFile, "rw");
                raf.setLength(chunkData.length
                        * Constants.REGION_LEN_X
                        * Constants.REGION_LEN_Y);

                {
                    int x = location.x % Constants.REGION_LEN_X;
                    int y = location.y % Constants.REGION_LEN_Y;

                    int offset = (y * Constants.REGION_LEN_X * chunkData.length) + (x * chunkData.length);

                    raf.seek(offset);
                }
                raf.write(chunkData);
                raf.close();
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}