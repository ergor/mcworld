package st.netb.mc.mcworld.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import st.netb.mc.mcworld.MainRenderer;

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
            MainRenderer mainRenderer = new MainRenderer();
            mainRenderer.init("oslo_laserscan_2019/data/dom", false);
            mainRenderer.renderGif();
            mainRenderer.renderAnvil();
            sender.sendMessage("§&§lTask completed successfully!");
        });
        return true;
    }
}