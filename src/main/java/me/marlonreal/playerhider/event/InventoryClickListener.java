package me.marlonreal.playerhider.event;

import me.marlonreal.playerhider.PlayerHider;
import me.marlonreal.playerhider.manager.PlayerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener {

    private final PlayerManager playerManager;

    public InventoryClickListener(PlayerHider plugin) {
        this.playerManager = plugin.getPlayerManager();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (playerManager.getModeForItem(e.getCurrentItem()) != -1)
            e.setCancelled(true);
    }
}