
package no.netb.mc.mcworld;

import java.io.File;
import java.nio.file.Path;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import no.netb.mc.mcworld.datastructs.minecraft.Coord2D;
import no.netb.mc.mcworld.datastructs.raw.World;
import no.netb.mc.mcworld.datastructs.raw.Tuple;
import no.netb.mc.mcworld.datastructs.raw.WorldSection;
import no.netb.mc.mcworld.datasource.FileType;
import no.netb.mc.mcworld.datasource.DataSource;
import no.netb.mc.mcworld.rendering.AnvilRenderer;
import no.netb.mc.mcworld.rendering.GifRenderer;
import no.netb.mc.mcworld.rendering.IntermediateOutput;


public class Main {

    public static void main(String[] args) {

        World world = getWorld(FileType.SOSI, new File(args[0]).toPath());
        boolean fastRender = Arrays.asList(args).contains("-f");

        List<WorldSection> worldSections = world.getSections();

        Map<Coord2D.Chunk, ChunkBuilder> incompleteChunks = new HashMap<>();

        File temporaryDir = new File("tmp");

        if (!fastRender) {
            IntermediateOutput ioWriter = new IntermediateOutput(temporaryDir);

            for (WorldSection worldSection : worldSections) {
                Tuple<List<ChunkBuilder>> chunkBuilders =
                        WorldMapper.toChunkBuilders(world, worldSection, incompleteChunks);

                List<ChunkBuilder> completeChunks = chunkBuilders.first();
                List<ChunkBuilder> intersectingChunks = chunkBuilders.second();

                List<ChunkBuilder> updatedChunks = incompleteChunks.values().stream()
                        .filter(ChunkBuilder::isComplete)
                        .collect(Collectors.toList());

                updatedChunks.forEach(cs -> {
                    completeChunks.add(cs);
                    incompleteChunks.remove(cs.getChunkLocation());
                });

                intersectingChunks.forEach(cs ->
                        incompleteChunks.put(cs.getChunkLocation(), cs));

                ioWriter.writeFiles(completeChunks);

                System.out.println("rendered " + completeChunks.size() + " chunks, " + intersectingChunks.size() + " on hold");
            }
        }
        else {
            System.out.println("Warning: -f was specified. Will only re-render based on existing processed data");
        }

        System.out.println("rendering GIF...");
        GifRenderer gif = new GifRenderer(temporaryDir, new File("out"));
        gif.render();

        System.out.println("rendering minecraft world...");
        AnvilRenderer anvilRenderer = new AnvilRenderer(temporaryDir, new File("anvil"));
        anvilRenderer.render();

        System.out.println("done");
    }


    private static World getWorld(FileType dataFileType, Path dataDirectory) {

        List<File> dataSourceFiles = Arrays.stream(dataDirectory.toFile().listFiles())
                .filter(p -> p.isFile() && p.getName().endsWith(dataFileType.getExtension()))
                .sorted()
                .collect(Collectors.toList());

        DataSource dataSource = DataSource.getInstance(dataFileType, dataSourceFiles);

        return new World(dataSource.getWorldSections());
    }
}
