package com.example.lab5;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataOperations {

    public void insertGame(String gameTitle) throws SQLException {
        if (gameExists(gameTitle)) {
            throw new SQLException("Game already exists in the database.");
        }
        String sql = "INSERT INTO game (game_title) VALUES (?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, gameTitle);
            pstmt.executeUpdate();
        }
    }

    public boolean gameExists(String gameTitle) {
        String sql = "SELECT game_id FROM game WHERE game_title = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, gameTitle);
            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // Returns true if a game with the given title exists
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public void insertPlayer(String firstName, String lastName, String address, String postalCode, String province, String phoneNumber) throws SQLException {
        if (playerExists(firstName, lastName)) {
            throw new SQLException("Player already exists in the database.");
        }
        String sql = "INSERT INTO player (first_name, last_name, address, postal_code, province, phone_number) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, address);
            pstmt.setString(4, postalCode);
            pstmt.setString(5, province);
            pstmt.setString(6, phoneNumber);
            pstmt.executeUpdate();
        }
    }

    public boolean playerExists(String firstName, String lastName) {
        String sql = "SELECT player_id FROM player WHERE first_name = ? AND last_name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // Returns true if a player with the given name exists
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void updatePlayer(int playerId, String firstName, String lastName, String address, String postalCode, String province, String phoneNumber) {
        String sql = "UPDATE player SET first_name = ?, last_name = ?, address = ?, postal_code = ?, province = ?, phone_number = ? WHERE player_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, address);
            pstmt.setString(4, postalCode);
            pstmt.setString(5, province);
            pstmt.setString(6, phoneNumber);
            pstmt.setInt(7, playerId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Player getPlayerInfo(int playerId) {
        Player player = null;
        String sql = "SELECT * FROM player WHERE player_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, playerId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                player = new Player(
                        rs.getInt("player_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("address"),
                        rs.getString("postal_code"),
                        rs.getString("province"),
                        rs.getString("phone_number")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return player;
    }
}
