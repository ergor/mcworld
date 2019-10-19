package st.netb.mc.mcworld.datastructs.minecraft;

import st.netb.mc.mcworld.Constants;
import st.netb.mc.mcworld.datastructs.raw.Tuple;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class MinecraftCoordinate {

    int x;
    int z;

    public MinecraftCoordinate(int x, int z) {
        this.x = x;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public static Tuple<MinecraftCoordinate> blockInRegion(MinecraftCoordinate blockInWorld) {
        throw new NotImplementedException();
    }

    /**
     *
     * @param blockInWorld the coordinate of the block in the world
     * @return the blocks's coordinate in its chunk, and the coordinate of that chunk in the world
     */
    private static Tuple<MinecraftCoordinate> blockInChunk(MinecraftCoordinate blockInWorld) {
        int blockX = blockInWorld.x % Constants.CHUNK_LEN_X;
        int blockZ = blockInWorld.z % Constants.CHUNK_LEN_Z;

        int chunkX = blockInWorld.x / Constants.CHUNK_LEN_X;
        int chunkZ = blockInWorld.z / Constants.CHUNK_LEN_Z;

        return new Tuple<>(
                new MinecraftCoordinate(blockX, blockZ),
                new MinecraftCoordinate(chunkX, chunkZ)
        );
    }

    /**
     * @param chunkInWorld the coordinate of the chunk in the world
     * @return the chunk's coordinate in its region, and the coordinate of that region in the world
     */
    private static Tuple<MinecraftCoordinate> chunkInRegion(MinecraftCoordinate chunkInWorld) {
        int chunkX = chunkInWorld.x % Constants.REGION_LEN_X;
        int chunkZ = chunkInWorld.z % Constants.REGION_LEN_Z;

        int regionX = chunkInWorld.x / Constants.REGION_LEN_X;
        int regionZ = chunkInWorld.z / Constants.REGION_LEN_Z;

        return new Tuple<>(
                new MinecraftCoordinate(chunkX, chunkZ),
                new MinecraftCoordinate(regionX, regionZ)
        );
    }

    /**
     * Stolen from {@link java.awt.geom.Point2D#hashCode()}
     */
    @Override
    public int hashCode() {
        long bits = Double.doubleToLongBits(x);
        bits ^= Double.doubleToLongBits(z) * 31;
        return (((int) bits) ^ ((int) (bits >> 32)));
    }

    /**
     * Modified from {@link java.awt.geom.Point2D#equals(Object)}
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MinecraftCoordinate) {
            MinecraftCoordinate that = (MinecraftCoordinate) obj;
            return (x == that.x) && (z == that.z);
        }
        return super.equals(obj);
    }
}
