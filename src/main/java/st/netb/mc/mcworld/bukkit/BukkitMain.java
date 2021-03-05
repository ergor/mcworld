package st.netb.mc.mcworld.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

/**
 * @author Alexander Sagen
 * @date 2021-03-05
 */
public class BukkitMain extends JavaPlugin {

    private static BukkitMain plugin;

    public static BukkitMain getPlugin() {
        return plugin;
    }

    @Override
    public void onLoad() {
        plugin = this;
        Bukkit.getConsoleSender().sendMessage("§6§l[Mc World] §7->    §fLoading....");
    }

    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage("§6§l[Mc World] §7->    §fStarting up....");
        Objects.requireNonNull(getCommand("renderworld")).setExecutor(new CommandRenderWorld());
        Objects.requireNonNull(getCommand("unloadchunks")).setExecutor(new CommandUnloadChunks());
        Bukkit.getPluginManager().registerEvents(new WorldEventListener(), this);
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("§6§l[Mc World] §7->    §fDisabling....");
    }

    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        return new WorldGenerator();
    }
}
