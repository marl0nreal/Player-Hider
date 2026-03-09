package me.marlonreal.playerhider.event;

import me.marlonreal.playerhider.PlayerHider;
import me.marlonreal.playerhider.manager.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final PlayerHider plugin;
    private final PlayerManager playerManager;

    public PlayerJoinListener(PlayerHider plugin) {
        this.plugin = plugin;
        this.playerManager = plugin.getPlayerManager();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        for (Player other : Bukkit.getOnlinePlayers()) {
            if (other == p) continue;
            if (playerManager.getMode(other.getUniqueId()) != 1) {
                other.hidePlayer(plugin, p);
            }
        }

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            int m = plugin.getStorage().loadMode(p.getUniqueId());

            Bukkit.getScheduler().runTask(plugin, () -> {
                playerManager.setMode(p.getUniqueId(), m);
                playerManager.giveItem(p);
                playerManager.updateVisibility(p);

                for (Player other : Bukkit.getOnlinePlayers()) {
                    if (other == p) continue;
                    playerManager.updateVisibilityForPlayer(other, p);
                }
            });
        });
    }
}