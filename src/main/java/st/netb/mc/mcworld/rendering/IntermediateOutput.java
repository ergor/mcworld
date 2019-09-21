package st.netb.mc.mcworld.rendering;

import st.netb.mc.mcworld.Constants;
import st.netb.mc.mcworld.ChunkBuilder;
import st.netb.mc.mcworld.coordinates.ChunkLocation;
import st.netb.mc.mcworld.coordinates.MinecraftLocation;
import st.netb.mc.mcworld.coordinates.RegionLocation;
import st.netb.mc.mcworld.datastructs.raw.ChunkHeightmap;
import st.netb.mc.mcworld.datastructs.raw.Tuple;

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

            ChunkHeightmap chunk;
            try {
                chunk = chunkBuilder.build();
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            byte[] chunkData = chunk.getBytes();

            try {
                Tuple<MinecraftLocation> locationTuple = chunk.getLocation().referencedToRegion();
                ChunkLocation chunkLocation = (ChunkLocation) locationTuple.first();
                RegionLocation regionLocation = (RegionLocation) locationTuple.second();

                String fileName = regionLocation.z + "-" + regionLocation.x;

                File outputFile = new File(Paths.get(outputDir.getPath(), fileName).toString());

                RandomAccessFile raf = new RandomAccessFile(outputFile, "rw");
                raf.setLength(chunkData.length
                        * Constants.REGION_LEN_X
                        * Constants.REGION_LEN_Z);

                int offset = (chunkLocation.z * Constants.REGION_LEN_X * chunkData.length)
                        + (chunkLocation.x * chunkData.length);

                raf.seek(offset);
                raf.write(chunkData);
                raf.close();
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
