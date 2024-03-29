package no.netb.mc.mcworld.rendering;

import net.querz.nbt.mca.Chunk;
import net.querz.nbt.mca.MCAFile;
import net.querz.nbt.mca.MCAUtil;
import no.netb.mc.mcworld.Constants;
import no.netb.mc.mcworld.datastructs.minecraft.Block;
import no.netb.mc.mcworld.datastructs.minecraft.Coord2D;
import no.netb.mc.mcworld.datastructs.raw.ChunkHeightmap;
import no.netb.mc.mcworld.datastructs.raw.RegionHeightmap;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;

public class AnvilRenderer extends Renderer {

    private static final int SEA_LEVEL = 10;

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

        Map<File, Coord2D.Region> regionLocationMap = mapToRegions(
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

        int regionX = regionHeightmap.getLocation().getX();
        int regionZ = regionHeightmap.getLocation().getZ();
        MCAFile region = new MCAFile(regionX, regionZ);

        for (int z = 0; z < Constants.REGION_LEN_Z; z++) {
            for (int x = 0; x < Constants.REGION_LEN_X; x++) {

                Coord2D.Chunk location = new Coord2D.Chunk(x, z);
                ChunkHeightmap chunkHeightmap = regionHeightmap.getChunk(location);

                Chunk chunk = renderChunk(chunkHeightmap);

                region.setChunk(x, z, chunk);
            }
        }

        MCAUtil.writeMCAFile(region,
                Paths.get(outputDir.getPath(), MCAUtil.createNameFromRegionLocation(regionX, regionZ))
                        .toFile(), true);
    }

    private Chunk renderChunk(ChunkHeightmap chunkHeightmap) {

        Chunk chunk = Chunk.newChunk();

        for (int z = 0; z < Constants.CHUNK_LEN_Z; z++) {
            for (int x = 0; x < Constants.CHUNK_LEN_X; x++) {

                int height = (chunkHeightmap.getHeight(x, z) + SEA_LEVEL) & 0xFF;

                if (height > SEA_LEVEL) {
                    for (int i = 0; i < 2 && height >= 0; i++) {
                        chunk.setBlockStateAt(x, height--, z, Block.GRASS, false);
                    }
                    for (; height >= 0; height--) {
                        chunk.setBlockStateAt(x, height, z, Block.STONE, false);
                    }
                }
                else {
                    for (; height >= 0; height--) {
                        chunk.setBlockStateAt(x, height, z, Block.WATER, false);
                    }
                }
            }
        }

        chunk.cleanupPalettesAndBlockStates();
        return chunk;
    }
}
