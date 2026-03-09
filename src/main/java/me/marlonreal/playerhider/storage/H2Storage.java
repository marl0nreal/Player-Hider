package me.marlonreal.playerhider.storage;

import java.sql.*;
import java.util.UUID;

public class H2Storage implements Storage {

    private final Connection connection;

    public H2Storage(String filePath) throws SQLException {

        try {
            Class.forName("me.marlonreal.playerhider.libs.h2.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("H2 driver class not found", e);
        }

        connection = DriverManager.getConnection("jdbc:h2:" + filePath + ";AUTO_SERVER=TRUE");

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS playerhider (uuid VARCHAR(36) PRIMARY KEY, mode INT)");
        }
    }

    @Override
    public void saveMode(UUID uuid, int mode) {
        try (PreparedStatement ps = connection.prepareStatement(
                "MERGE INTO playerhider KEY(uuid) VALUES (?, ?)"
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