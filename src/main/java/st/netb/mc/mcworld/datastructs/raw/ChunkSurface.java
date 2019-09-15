package st.netb.mc.mcworld.datastructs.raw;


import st.netb.mc.mcworld.Constants;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ChunkSurface {

    private Point chunkLocation;
    // [y][x] organization is cache-optimized when iterating by x first (ie line by line)
    private float[][] surface = new float[Constants.CHUNK_LEN_Y][Constants.CHUNK_LEN_X];
    // maps each location to how many times there has been value insertions
    private Map<Point, Integer> insertions = new HashMap<>();
    private boolean isCompiled = false;


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

    public boolean isIncomplete() {
        return !isComplete();
    }

    public void insert(Point blockLocation, float height) {
        // add the value; we'll get the average of all additions later
        surface[blockLocation.y][blockLocation.x] += height;
        Integer ins = insertions.computeIfAbsent(blockLocation, key -> 0);
        insertions.put(blockLocation, ins + 1);
    }

    /**
     * Compiles the surface and returns it if all data is ready.
     * Otherwise throws exception.
     * <br>
     * The surface is organized as [y][x] to provide cache-optimizations
     * when iterating by x first (ie line by line).
     *
     * @return The surface height map
     * @throws Exception The chunk has blocks that haven't been inserted
     */
    public float[][] getSurface() throws Exception {

        if (isIncomplete()) {
            throw new Exception("cannot compile chunk surface; chunk has missing data");
        }

        if (!isCompiled) {
            for (int y = 0; y < Constants.CHUNK_LEN_Y; y++) {
                for (int x = 0; x < Constants.CHUNK_LEN_X; x++) {
                    Point point = new Point(x, y);
                    int n = insertions.get(point);
                    surface[y][x] /= n; // get average of all insertions at this location

                    if (surface[y][x] < 0.0f) {
                        surface[y][x] = 0.0f;
                    }
                }
            }

            isCompiled = true;
        }

        return surface;
    }
}
