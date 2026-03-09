package me.marlonreal.playerhider.event;

import me.marlonreal.playerhider.PlayerHider;
import me.marlonreal.playerhider.manager.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    private final PlayerHider plugin;
    private final PlayerManager playerManager;

    public PlayerQuitListener(PlayerHider plugin) {
        this.plugin = plugin;
        this.playerManager = plugin.getPlayerManager();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        int playerMode = playerManager.getMode(p.getUniqueId());

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () ->
                plugin.getStorage().saveMode(p.getUniqueId(), playerMode)
        );

        playerManager.removeMode(p.getUniqueId());
        playerManager.removeCooldown(p.getUniqueId());
    }
}