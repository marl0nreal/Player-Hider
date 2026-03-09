package me.marlonreal.playerhider.storage;


import com.zaxxer.hikari.HikariDataSource;
import me.marlonreal.playerhider.PlayerHider;
import me.marlonreal.playerhider.manager.ConfigManager;

import java.sql.*;
import java.util.UUID;

public class MySQLStorage implements Storage {

    private final HikariDataSource hikari;

    public MySQLStorage(PlayerHider plugin) throws Exception {
        ConfigManager config = plugin.getConfigManager();

        hikari = new HikariDataSource();
        hikari.setDataSourceClassName("com.mysql.cj.jdbc.MysqlDataSource");
        hikari.addDataSourceProperty("serverName", config.getMysqlHost());
        hikari.addDataSourceProperty("port", config.getMysqlPort());
        hikari.addDataSourceProperty("databaseName", config.getMysqlDatabase());
        hikari.addDataSourceProperty("user", config.getMysqlUsername());
        hikari.addDataSourceProperty("password", config.getMysqlPassword());

        try (Connection connection = hikari.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "CREATE TABLE IF NOT EXISTS playerhider (uuid VARCHAR(36) PRIMARY KEY, mode INT)"
             )) {
            ps.executeUpdate();
        }
    }

    @Override
    public void saveMode(UUID uuid, int mode) {
        try (Connection connection = hikari.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "REPLACE INTO playerhider (uuid, mode) VALUES (?, ?)"
             )) {
            ps.setString(1, uuid.toString());
            ps.setInt(2, mode);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int loadMode(UUID uuid) {
        try (Connection connection = hikari.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "SELECT mode FROM playerhider WHERE uuid=?"
             )) {
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("mode");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1;
    }

    @Override
    public void close() {
        hikari.close();
    }
}