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
 * Writes the height of each block at surface position (x, z)
 * as a single byte in an intermediate file.
 * The intermediate files are orginazied in regions like the
 * anvil world format. Each region is comprised of 32x32 chunks,
 * and each chunk is comprised of 16x16 blocks.
 *
 * <h2>Data organization</h2>
 * <h3>Chunk</h3>
 * Byte0 corresponds to (0, 0), and byte255 to (15, 15).
 * The bytes are saved line by line, ie. the first 16 bytes
 * correspond to (0..15, 0), the next 16 bytes (0..15, 1) etc.
 * <h3>Region</h3>
 * Byte 0..255 corresponds to chunk0, byte 256..511 to chunk1 etc.
 *
 * <h2>Output</h2>
 * The output filename shall follow the pattern of y-x.
 * For example, the region at (0, 5) will get the name "5-0"
 */
public class IntermediateOutput {

    public static final Pattern tempFilePattern = Pattern.compile("(\\d+)-(\\d+)");
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

                int offset = (chunkLocation.z * Constants.REGION_LEN_Z * chunkData.length)
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
