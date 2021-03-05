package st.netb.mc.mcworld;

import java.util.*;

public class Main {

    public static void main(String... args) {
        MainRenderer renderer = new MainRenderer();
        renderer.init(args[0], Arrays.asList(args).contains("-f"));
        renderer.renderGif();
        renderer.renderAnvil();
    }
}
