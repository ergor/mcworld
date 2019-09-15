package st.netb.mc.mcworld.formats;

public enum FileType {

    SOSI(".sos");

    private String extension;

    FileType(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }
}
