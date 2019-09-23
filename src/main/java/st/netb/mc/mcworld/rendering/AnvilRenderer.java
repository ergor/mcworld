package st.netb.mc.mcworld.rendering;

import net.querz.nbt.CompoundTag;
import net.querz.nbt.mca.Chunk;
import net.querz.nbt.mca.MCAFile;
import net.querz.nbt.mca.MCAUtil;
import st.netb.mc.mcworld.Constants;
import st.netb.mc.mcworld.datastructs.minecraft.coordinates.ChunkLocation;
import st.netb.mc.mcworld.datastructs.minecraft.coordinates.RegionLocation;
import st.netb.mc.mcworld.datastructs.minecraft.coordinates.referenceframe.ReferenceFrame;
import st.netb.mc.mcworld.datastructs.raw.ChunkHeightmap;
import st.netb.mc.mcworld.datastructs.raw.RegionHeightmap;
import st.netb.mc.mcworld.datastructs.raw.Tuple;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;

public class AnvilRenderer extends Renderer {

    private final File intermediateDir;
    private final File outputDir;

    public AnvilRenderer(File intermediateDir, File outputDir) {
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

        Map<File, RegionLocation> regionLocationMap = mapToRegions(
                Arrays.asList(intermediateDir.listFiles()));

        for (File file : regionLocationMap.keySet()) {
            try {
                RegionHeightmap region = new RegionHeightmap(
                        regionLocationMap.get(file),
                        Files.readAllBytes(file.toPath()));

                renderRegion(region);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void renderRegion(RegionHeightmap regionHeightmap) throws IOException {
        for (int z = 0; z < Constants.REGION_LEN_Z; z++) {
            for (int x = 0; x < Constants.REGION_LEN_X; x++) {

                int regionX = regionHeightmap.getLocation().getX();
                int regionZ = regionHeightmap.getLocation().getZ();

                MCAFile region = new MCAFile(regionX, regionZ);

                ChunkLocation location = new ChunkLocation(x, z, ReferenceFrame.REGION);
                ChunkHeightmap chunkHeightmap = regionHeightmap.getChunk(location);

                Chunk chunk = renderChunk(chunkHeightmap);

                region.setChunk(x, z, chunk);

                MCAUtil.writeMCAFile(region,
                        Paths.get(outputDir.getPath(), MCAUtil.createNameFromRegionLocation(regionX, regionZ))
                                .toFile(), true);
            }
        }
    }

    private Chunk renderChunk(ChunkHeightmap chunkHeightmap) {

        Chunk chunk = Chunk.newChunk();

        for (int z = 0; z < Constants.CHUNK_LEN_Z; z++) {
            for (int x = 0; x < Constants.CHUNK_LEN_X; x++) {

                int height = chunkHeightmap.getHeight(x, z);

                CompoundTag grass = new CompoundTag();
                grass.putString("Name", "minecraft:grass_block");
                chunk.setBlockStateAt(x, height, z, grass, false);
            }
        }

        chunk.cleanupPalettesAndBlockStates();
        return chunk;
    }
}
