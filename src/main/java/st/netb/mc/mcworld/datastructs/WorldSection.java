package st.netb.mc.mcworld.datastructs;

import st.netb.mc.mcworld.Constants;
import st.netb.mc.mcworld.math.Point3D;

public class WorldSection {

    // orginaized as:
    // [x], [z], [y]; where y is altitude
    private Block[][][] blocks;

    public WorldSection(int width, int height) {
        blocks = new Block[width][height][Constants.MAP_HEIGHT_RANGE];
    }

    public void insertBlock(Block block) {
        Point3D position = block.getLocation();
        blocks[position.getX()][position.getZ()][position.getY()] = block;
    }
}
