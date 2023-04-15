package cx.leo.rankup.listeners;

import cx.leo.rankup.RankupPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final RankupPlugin plugin;

    public PlayerJoinListener(RankupPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        plugin.getRankupManager().loadRank(event.getPlayer());
    }

}
