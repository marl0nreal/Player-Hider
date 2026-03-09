package me.marlonreal.playerhider.storage;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;
import java.util.UUID;

public class H2Storage implements Storage {

    private final HikariDataSource hikari;


    public H2Storage(String filePath) throws SQLException {
        hikari = new HikariDataSource();
        hikari.setJdbcUrl("jdbc:h2:" + filePath);
        hikari.setMaximumPoolSize(5);

        try (Connection connection = hikari.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS playerhider (uuid VARCHAR(36) PRIMARY KEY, mode INT)"
            );
        }
    }

    @Override
    public void saveMode(UUID uuid, int mode) {
        try (Connection connection = hikari.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "MERGE INTO playerhider (uuid, mode) KEY(uuid) VALUES (?, ?)"
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
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("mode");
            }
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