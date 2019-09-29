package st.netb.mc.mcworld.rastersource;

import st.netb.mc.mcworld.rastersource.esri.TiffWorldFileSource;
import st.netb.mc.mcworld.rastersource.kartverket.SosiSource;

import java.io.File;
import java.util.List;
import java.util.function.Function;

public enum FileType {

    SOSI(".sos", SosiSource::new),
    TFW(".tfw", TiffWorldFileSource::new);

    private String extension;
    Function<List<File>, RasterSource> constructor;

    FileType(String extension, Function<List<File>, RasterSource> constructor) {
        this.extension = extension;
        this.constructor = constructor;
    }

    public String getExtension() {
        return extension;
    }
}
