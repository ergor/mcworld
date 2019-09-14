package st.netb.mc.mcworld.datastructs.raw;


import java.awt.geom.Rectangle2D;
import java.awt.image.Raster;

public class WorldSection {

    private Rectangle2D.Float area;
    private Raster raster;

    public WorldSection(Raster raster, float northingMin, float northingMax, float eastingMin, float eastingMax) {

        /*
         x – the X coordinate of the upper-left corner of the newly constructed Rectangle2D
         y – the Y coordinate of the upper-left corner of the newly constructed Rectangle2D
         w – the width of the newly constructed Rectangle2D
         h – the height of the newly constructed Rectangle2D
         */

        float x = eastingMin;
        float y = northingMax;

        float w = eastingMax - eastingMin;
        float h = northingMax - northingMin;

        area = new Rectangle2D.Float(x, y, w, h);
        this.raster = raster;
    }
}
