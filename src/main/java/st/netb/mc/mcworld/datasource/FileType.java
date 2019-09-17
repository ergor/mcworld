package st.netb.mc.mcworld.datasource;

import java.io.File;
import java.util.List;
import java.util.function.Function;

public enum FileType {

    SOSI(".sos", SosiFormat::new);

    private String extension;
    Function<List<File>, DataSource> initializer;

    FileType(String extension, Function<List<File>, DataSource> initializer) {
        this.extension = extension;
        this.initializer = initializer;
    }

    public String getExtension() {
        return extension;
    }
}
