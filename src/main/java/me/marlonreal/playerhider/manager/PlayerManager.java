package me.marlonreal.playerhider.manager;

import me.marlonreal.playerhider.PlayerHider;
import me.marlonreal.playerhider.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerManager {

    private final PlayerHider plugin;

    private final Map<UUID, Integer> mode = new ConcurrentHashMap<>();
    private final Map<UUID, Long> cooldown = new ConcurrentHashMap<>();

    public PlayerManager(PlayerHider plugin) {
        this.plugin = plugin;
    }

    public void setMode(UUID uuid, int mode) {
        this.mode.put(uuid, mode);
    }

    public int getMode(UUID uuid) {
        return mode.getOrDefault(uuid, 1);
    }

    public void removeMode(UUID uuid) {
        mode.remove(uuid);
    }

    public boolean isOnCooldown(UUID uuid) {
        if (!cooldown.containsKey(uuid)) return false;
        return System.currentTimeMillis() - cooldown.get(uuid) < plugin.getConfigManager().getClickCooldown();
    }

    public void setCooldown(UUID uuid) {
        cooldown.put(uuid, System.currentTimeMillis());
    }

    public void removeCooldown(UUID uuid) {
        cooldown.remove(uuid);
    }

    public void giveItem(Player p) {
        ConfigManager config = plugin.getConfigManager();
        if (!p.getWorld().getName().equalsIgnoreCase(config.getWorld())) return;

        int m = getMode(p.getUniqueId());
        String section = m == 1 ? "items.all" : m == 2 ? "items.none" : "items.vip";

        ItemStack item = new ItemBuilder(plugin.getConfig().getConfigurationSection(section)).build();
        p.getInventory().setItem(config.getSlot(), item);
    }

    public int getModeForItem(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return -1;
        String name = item.getItemMeta().getDisplayName();
        ConfigManager config = plugin.getConfigManager();

        if (name.equals(config.getAllItemName())) return 1;
        if (name.equals(config.getNoneItemName())) return 2;
        if (name.equals(config.getVipItemName())) return 3;
        return -1;
    }

    public void updateVisibility(Player p) {
        int m = getMode(p.getUniqueId());

        for (Player target : Bukkit.getOnlinePlayers()) {
            if (target == p) continue;
            if (m == 1) p.showPlayer(plugin, target);
            else if (m == 2) p.hidePlayer(plugin, target);
            else {
                if (target.hasPermission(ConfigManager.VIP_PERMISSION)) p.showPlayer(plugin, target);
                else p.hidePlayer(plugin, target);
            }
        }
    }

    public void updateVisibilityForPlayer(Player viewer, Player target) {
        int viewerMode = getMode(viewer.getUniqueId());

        if (viewerMode == 1) viewer.showPlayer(plugin, target);
        else if (viewerMode == 2) viewer.hidePlayer(plugin, target);
        else {
            if (target.hasPermission(ConfigManager.VIP_PERMISSION)) viewer.showPlayer(plugin, target);
            else viewer.hidePlayer(plugin, target);
        }
    }

    public void playSound(Player p) {
        ConfigManager config = plugin.getConfigManager();
        p.playSound(p.getLocation(), config.getSound(), config.getSoundVolume(), config.getSoundPitch());
    }
}