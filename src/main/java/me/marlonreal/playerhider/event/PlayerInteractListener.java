package me.marlonreal.playerhider.event;

import me.marlonreal.playerhider.PlayerHider;
import me.marlonreal.playerhider.manager.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {

    private final PlayerManager playerManager;

    public PlayerInteractListener(PlayerHider plugin) {
        this.playerManager = plugin.getPlayerManager();
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (playerManager.isOnCooldown(p.getUniqueId())) return;

        int currentMode = playerManager.getModeForItem(p.getInventory().getItemInMainHand());
        if (currentMode == -1) return;

        int nextMode = (currentMode % 3) + 1;

        playerManager.setCooldown(p.getUniqueId());
        playerManager.setMode(p.getUniqueId(), nextMode);
        playerManager.playSound(p);
        playerManager.giveItem(p);
        playerManager.updateVisibility(p);
    }
}