package st.netb.mc.mcworld.bukkit;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

/**
 * @author Alexander Sagen
 * @date 2021-03-05
 */
public class WorldEventListener implements Listener {

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent e) {
        e.setSaveChunk(false);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        p.setGameMode(GameMode.CREATIVE);
        p.setAllowFlight(true);
        p.setFlying(true);
        p.setOp(true);
        e.getPlayer().teleport(new Location(e.getPlayer().getWorld(), 8319, 108, 13403, -100, 51));
    }

}
