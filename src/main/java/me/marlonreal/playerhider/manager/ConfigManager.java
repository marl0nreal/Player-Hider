package me.marlonreal.playerhider.manager;

import me.marlonreal.playerhider.PlayerHider;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ConfigManager {

    public static final String VIP_PERMISSION = "playerhider.vip";

    private final String world;
    private final int slot;
    private final long clickCooldown;
    private final String storageType;
    private final Sound sound;
    private final float soundVolume;
    private final float soundPitch;

    private final String mysqlHost;
    private final int mysqlPort;
    private final String mysqlDatabase;
    private final String mysqlUsername;
    private final String mysqlPassword;

    private final String allItemName;
    private final String noneItemName;
    private final String vipItemName;

    public ConfigManager(PlayerHider plugin) {
        plugin.getDataFolder().mkdirs();

        File configFile = new File(plugin.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            plugin.saveResource("config.yml", false);
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        world = config.getString("world");
        slot = config.getInt("slot");
        clickCooldown = config.getLong("click-cooldown");
        storageType = config.getString("storage.type");
        sound = Sound.valueOf(config.getString("sound.name"));
        soundVolume = (float) config.getDouble("sound.volume");
        soundPitch = (float) config.getDouble("sound.pitch");

        allItemName = color(config.getString("items.all.display-name"));
        noneItemName = color(config.getString("items.none.display-name"));
        vipItemName = color(config.getString("items.vip.display-name"));

        mysqlHost = config.getString("mysql.host");
        mysqlPort = config.getInt("mysql.port");
        mysqlDatabase = config.getString("mysql.database");
        mysqlUsername = config.getString("mysql.username");
        mysqlPassword = config.getString("mysql.password");
    }

    public String getWorld() { return world; }
    public int getSlot() { return slot; }

    public long getClickCooldown() { return clickCooldown; }
    public String getStorageType() { return storageType; }
    public Sound getSound() { return sound; }
    public float getSoundVolume() { return soundVolume; }
    public float getSoundPitch() { return soundPitch; }

    public String getMysqlHost() { return mysqlHost; }
    public int getMysqlPort() { return mysqlPort; }
    public String getMysqlDatabase() { return mysqlDatabase; }
    public String getMysqlUsername() { return mysqlUsername; }
    public String getMysqlPassword() { return mysqlPassword; }

    public String getAllItemName() { return allItemName; }
    public String getNoneItemName() { return noneItemName; }
    public String getVipItemName() { return vipItemName; }

    private String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}