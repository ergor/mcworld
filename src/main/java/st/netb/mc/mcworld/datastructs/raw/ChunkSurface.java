package st.netb.mc.mcworld.datastructs.raw;


import st.netb.mc.mcworld.Constants;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ChunkSurface {

    private float[][] surface = new float[Constants.CHUNK_LEN_X][Constants.CHUNK_LEN_Y];

    // maps each location to how many times there has been value insertions
    private Map<Point, Integer> insertions = new HashMap<>();

    private float[][] compiledSurface = null;

    private Point chunkLocation;

    public ChunkSurface(Point chunkLocation) {
        this.chunkLocation = chunkLocation;
    }

    public Point getChunkLocation() {
        return chunkLocation;
    }

    /**
     * Checks whether all locations have been assigned a value at least once
     */
    public boolean isComplete() {
        return insertions.size() == Constants.CHUNK_LEN_X * Constants.CHUNK_LEN_Y;
    }

    public void insert(Point blockLocation, float height) {
        // add the value; we'll get the average of all additions later
        surface[blockLocation.x][blockLocation.y] += height;
        Integer ins = insertions.computeIfAbsent(blockLocation, key -> 0);
        insertions.put(blockLocation, ins + 1);
    }

    public float[][] getSurface() {
        if (compiledSurface == null) {
            compiledSurface = Arrays.stream(surface)
                    .map(float[]::clone)
                    .toArray(float[][]::new);

            for (int y = 0; y < Constants.CHUNK_LEN_Y; y++) {
                for (int x = 0; x < Constants.CHUNK_LEN_X; x++) {
                    Point point = new Point(x, y);
                    int n = insertions.get(point);
                    compiledSurface[x][y] = surface[x][y] / n; // get average of all insertions at this location

                    if (compiledSurface[x][y] < 0.0f) {
                        compiledSurface[x][y] = 0.0f;
                    }
                }
            }
        }

        return compiledSurface;
    }
}
