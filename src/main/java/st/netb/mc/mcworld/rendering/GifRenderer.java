package st.netb.mc.mcworld.rendering;

import java.io.File;

public class GifRenderer implements Renderer {

    private File intermediateDir;

    public GifRenderer(File intermediateDir) {
        this.intermediateDir = intermediateDir;
    }

    @Override
    public void render() {
        for (File file : intermediateDir.listFiles()) {

        }
    }
}
