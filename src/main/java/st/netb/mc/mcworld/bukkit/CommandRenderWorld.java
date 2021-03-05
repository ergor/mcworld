package st.netb.mc.mcworld.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import st.netb.mc.mcworld.Main;

/**
 * @author Alexander Sagen
 * @date 2021-03-05
 */
public class CommandRenderWorld implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage("§6§lStarting async thread....");
        Bukkit.getScheduler().scheduleAsyncDelayedTask(BukkitMain.getPlugin(), () -> {
            sender.sendMessage("§6§lYou called me, now prepare to be dissapointed");
            Main.main("dtm1");
            sender.sendMessage("§&§lTask completed successfully!");
        });
        return true;
    }
}
