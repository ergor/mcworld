package st.netb.mc.mcworld;

import st.netb.mc.mcworld.datastructs.raw.ChunkSurface;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

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

    private File outputDir;

    public IntermediateOutput(File outputDir) {
        this.outputDir = outputDir;
        if (!outputDir.exists()) {
            outputDir.mkdir();
        }
    }

    public void writeFiles(List<ChunkSurface> chunkSurfaces) {

        for (ChunkSurface chunkSurface : chunkSurfaces) {

            byte[] output = new byte[Constants.CHUNK_LEN_X * Constants.CHUNK_LEN_Y];
            Point location = chunkSurface.getChunkLocation();

            try {
                float[][] heightMap = chunkSurface.getSurface();
                for (int y = 0; y < Constants.CHUNK_LEN_Y; y++) {
                    for (int x = 0; x < Constants.CHUNK_LEN_X; x++) {
                        output[y*Constants.CHUNK_LEN_Y + x] = (byte) Math.ceil(heightMap[y][x]);
                    }
                }
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }

            try {
                String fileName = location.y + "-" + location.x;

                FileOutputStream fos = new FileOutputStream(
                        Paths.get(outputDir.getPath(), fileName).toString());

                fos.write(output);
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
