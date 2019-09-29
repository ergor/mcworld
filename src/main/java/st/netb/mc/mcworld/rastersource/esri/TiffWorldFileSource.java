package st.netb.mc.mcworld.rastersource.esri;

import st.netb.mc.mcworld.rastersource.RasterSource;
import st.netb.mc.mcworld.datastructs.raw.WorldSection;

import java.io.File;
import java.util.List;

public class TiffWorldFileSource implements RasterSource {

    public TiffWorldFileSource(List<File> files) {

    }

    @Override
    public List<WorldSection> getWorldSections() {
        return null;
    }
}
