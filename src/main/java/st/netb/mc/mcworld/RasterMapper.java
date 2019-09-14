package st.netb.mc.mcworld;

import st.netb.mc.mcworld.datastructs.minecraft.Block;

import java.awt.image.Raster;
import java.util.ArrayList;
import java.util.List;

public class RasterMapper {

    public static List<Block> heightMapMapper(Raster raster) {
        List<Block> blocks = new ArrayList<>();

        for (int y = 0; y < raster.getHeight(); y++) {
            for (int x = 0; x < raster.getWidth(); x++) {
                float height = raster.getPixel(x, y, (float[]) null)[0];
                blocks.add(Block.create(height, x, y));
            }
        }

        return blocks;
    }
}
