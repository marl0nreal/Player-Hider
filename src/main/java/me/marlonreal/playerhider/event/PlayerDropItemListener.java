package me.marlonreal.playerhider.event;

import me.marlonreal.playerhider.PlayerHider;
import me.marlonreal.playerhider.manager.PlayerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerDropItemListener implements Listener {

    private final PlayerManager playerManager;

    public PlayerDropItemListener(PlayerHider plugin) {
        this.playerManager = plugin.getPlayerManager();
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        if (playerManager.getModeForItem(e.getItemDrop().getItemStack()) != -1)
            e.setCancelled(true);
    }
}