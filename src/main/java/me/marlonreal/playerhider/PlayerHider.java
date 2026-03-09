package me.marlonreal.playerhider;

import me.marlonreal.playerhider.event.*;
import me.marlonreal.playerhider.manager.ConfigManager;
import me.marlonreal.playerhider.manager.PlayerManager;
import me.marlonreal.playerhider.storage.H2Storage;
import me.marlonreal.playerhider.storage.LocalStorage;
import me.marlonreal.playerhider.storage.MySQLStorage;
import me.marlonreal.playerhider.storage.Storage;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

// PlayerHider - made with ❤ by marlonreal
public final class PlayerHider extends JavaPlugin {

    private ConfigManager configManager;
    private PlayerManager playerManager;

    private Storage storage;

    @Override
    public void onEnable() {

        configManager = new ConfigManager(this);
        playerManager = new PlayerManager(this);

        switch (configManager.getStorageType().toUpperCase()) {
            case "MYSQL":
                try { storage = new MySQLStorage(this); }
                catch (Exception e) {
                    getLogger().severe("MySQL failed! Switching to LOCAL.");
                    e.printStackTrace();
                    storage = new LocalStorage(this);
                }
                break;

            case "H2":
                try {
                    String h2Path = new File(getDataFolder(), "data").getAbsolutePath();
                    storage = new H2Storage(h2Path);
                } catch (Exception e) {
                    getLogger().severe("H2 failed! Switching to LOCAL.");
                    e.printStackTrace();
                    storage = new LocalStorage(this);
                }
                break;

            case "LOCAL":
            default:
                storage = new LocalStorage(this);
                break;
        }

        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(new PlayerJoinListener(this), this);
        manager.registerEvents(new PlayerQuitListener(this), this);
        manager.registerEvents(new PlayerInteractListener(this), this);
        manager.registerEvents(new PlayerDropItemListener(this), this);
        manager.registerEvents(new InventoryClickListener(this), this);
    }

    @Override
    public void onDisable() {
        if (storage != null) {
            storage.close();
        }
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public Storage getStorage() {
        return storage;
    }
}
