package st.netb.mc.mcworld.datastructs.minecraft;

import st.netb.mc.mcworld.Constants;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Represents a coordinate in minecraft in the X and Z axis,
 * i.e. a point in the world north-south and east-west. Don't care about the altitude.
 * <ul>
 * <li> X: distance eastwards. </li>
 * <li> Z: distance southwards. </li>
 * </ul>
 */
public abstract class Coord2D {

    /**
     * Represents the coordinate of a block.
     */
    public static class Block extends Coord2D {
        public Block(int x, int z) {
            super(x, z);
        }
    }

    /**
     * Represents the coordinate of a chunk. A chunk consists of blocks.
     * The coordinate of the chunk at the origin of the world is (0, 0),
     * and the chunk to the east of it is (1, 0).
     */
    public static class Chunk extends Coord2D {
        public Chunk(int x, int z) {
            super(x, z);
        }
    }

    /**
     * Represents the coordinate of a region. A region consists of chunks.
     * The coordinate of the region at the origin of the world is (0, 0),
     * and the region to the east of it is (1, 0).
     */
    public static class Region extends Coord2D {
        public Region(int x, int z) {
            super(x, z);
        }
    }

    /**
     * A pair of coordinates where 'rel' is a position relative to 'abs',
     * and 'abs' is an absolute position in the world.
     */
    public static class Relative<R, A> {
        private final R rel;
        private final A abs;

        public Relative(R rel, A abs) {
            this.rel = rel;
            this.abs = abs;
        }

        public R getRel() {
            return rel;
        }

        public A getAbs() {
            return abs;
        }
    }

    int x; // east
    int z; // south

    public Coord2D(int x, int z) {
        this.x = x;
        this.z = z;
    }

    /**
     * The distance eastwards
     */
    public int getX() {
        return x;
    }

    /**
     * The distance southwards
     */
    public int getZ() {
        return z;
    }

    public static Relative<Block, Region> blockInRegion(Block blockInWorld) {
        throw new NotImplementedException();
    }

    /**
     * @param blockInWorld the absolute coordinate of the block in the world
     * @return the blocks's relative coordinate in its chunk, and the absolute coordinate of that chunk in the world
     */
    private static Relative<Block, Chunk> blockInChunk(Block blockInWorld) {
        int blockX = blockInWorld.x % Constants.CHUNK_LEN_X;
        int blockZ = blockInWorld.z % Constants.CHUNK_LEN_Z;

        int chunkX = blockInWorld.x / Constants.CHUNK_LEN_X;
        int chunkZ = blockInWorld.z / Constants.CHUNK_LEN_Z;

        return new Relative<>(
                new Block(blockX, blockZ),
                new Chunk(chunkX, chunkZ)
        );
    }

    /**
     * @param chunkInWorld the absolute coordinate of the chunk in the world
     * @return the chunk's coordinate in its region, and the coordinate of that region in the world
     */
    public static Relative<Chunk, Region> chunkInRegion(Chunk chunkInWorld) {
        int chunkX = chunkInWorld.x % Constants.REGION_LEN_X;
        int chunkZ = chunkInWorld.z % Constants.REGION_LEN_Z;

        int regionX = chunkInWorld.x / Constants.REGION_LEN_X;
        int regionZ = chunkInWorld.z / Constants.REGION_LEN_Z;

        return new Relative<>(
                new Chunk(chunkX, chunkZ),
                new Region(regionX, regionZ)
        );
    }

    public static Block toBlock(Chunk chunk) {
        return new Block(Constants.CHUNK_LEN_X * chunk.x, Constants.CHUNK_LEN_Z * chunk.z);
    }

    public static Chunk toChunk(Region region) {
        return new Chunk(Constants.REGION_LEN_X * region.x, Constants.REGION_LEN_Z * region.z);
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
        if (obj instanceof Coord2D) {
            Coord2D that = (Coord2D) obj;
            if ((this instanceof Block && that instanceof Block)
                    || (this instanceof Chunk && that instanceof Chunk)
                    || (this instanceof Region && that instanceof Region)) {
                return (x == that.x) && (z == that.z);
            }
            throw new IllegalArgumentException(String.format("Illegal XZ nested class comparison: tried to compare [%s] to [%s]", this.getClass().getName(), that.getClass().getName()));
        }
        return super.equals(obj);
    }
}
