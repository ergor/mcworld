package st.netb.mc.mcworld.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Alexander Sagen
 * @date 2021-03-05
 */
public class CommandUnloadChunks implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Bukkit.broadcastMessage("§6§lStarting unloading of chunks...");
        final AtomicInteger chunks = new AtomicInteger();
        final AtomicInteger failed = new AtomicInteger();
        Bukkit.getWorlds().forEach(w -> {
            Arrays.stream(w.getLoadedChunks()).forEach(c -> {
                if(!c.unload(false)) failed.incrementAndGet();
                chunks.incrementAndGet();
            });
        });
        Bukkit.broadcastMessage("§6§lUnloaded " + chunks.get() + " chunks, " + failed.get() + " failed!");
        return true;
    }
}
