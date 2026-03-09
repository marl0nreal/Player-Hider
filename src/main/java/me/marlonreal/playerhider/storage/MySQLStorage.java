package me.marlonreal.playerhider.storage;


import me.marlonreal.playerhider.PlayerHider;
import me.marlonreal.playerhider.manager.ConfigManager;

import java.sql.*;
import java.util.UUID;

public class MySQLStorage implements Storage {

    private final Connection connection;

    public MySQLStorage(PlayerHider plugin) throws Exception {

        try {
            Class.forName("me.marlonreal.playerhider.libs.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new Exception("MySQL driver class not found", e);
        }

        ConfigManager config = plugin.getConfigManager();

        connection = DriverManager.getConnection(
                "jdbc:mysql://" + config.getMysqlHost() + ":" + config.getMysqlPort() + "/" + config.getMysqlDatabase(),
                config.getMysqlUsername(),
                config.getMysqlPassword()
        );

        try (PreparedStatement ps = connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS playerhider (uuid VARCHAR(36) PRIMARY KEY, mode INT)"
        )) {
            ps.executeUpdate();
        }
    }

    @Override
    public void saveMode(UUID uuid, int mode) {
        try (PreparedStatement ps = connection.prepareStatement(
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
        try (PreparedStatement ps = connection.prepareStatement(
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
        try {
            connection.close();
        } catch (SQLException ignored) {}
    }
}