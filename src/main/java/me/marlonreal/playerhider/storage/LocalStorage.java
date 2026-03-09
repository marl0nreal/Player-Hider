package me.marlonreal.playerhider.storage;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.UUID;

public class LocalStorage implements Storage {

    private final File file;
    private final FileConfiguration config;

    public LocalStorage(JavaPlugin plugin) {

        file = new File(plugin.getDataFolder(), "playerdata.yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception ignored) {}
        }

        config = YamlConfiguration.loadConfiguration(file);
    }

    @Override
    public void saveMode(UUID uuid, int mode) {

        config.set(uuid.toString(), mode);

        try {
            config.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int loadMode(UUID uuid) {
        return config.getInt(uuid.toString(), 1);
    }

    @Override
    public void close() {}
}