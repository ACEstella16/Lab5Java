package com.example.lab5;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GameApp extends JFrame {
    public JTextField gameTitleField;
    public JButton insertGameButton;
    public JButton clearGameButton;

    public JComboBox<Integer> playerIdComboBox;
    public JButton showPlayerInfoButton;
    public JButton insertPlayerButton;
    public JButton clearPlayerButton;
    public JButton updatePlayerButton;

    public DataOperations dataOperations;

    public GameApp() {
        dataOperations = new DataOperations();

        setTitle("Game Application");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 1));

        // Game panel
        JPanel gamePanel = new JPanel();
        gamePanel.add(new JLabel("Game Title:"));
        gameTitleField = new JTextField(20);
        gamePanel.add(gameTitleField);
        insertGameButton = new JButton("Insert Game");
        gamePanel.add(insertGameButton);
        clearGameButton = new JButton("Clear");
        gamePanel.add(clearGameButton);

        insertGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String gameTitle = gameTitleField.getText();
                try {
                    dataOperations.insertGame(gameTitle);
                    JOptionPane.showMessageDialog(null, "Game inserted successfully!");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        clearGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameTitleField.setText("");
            }
        });

        add(gamePanel);

        // Player panel
        JPanel playerPanel = new JPanel(new GridLayout(3, 2));
        playerPanel.add(new JLabel("Player ID:"));
        playerIdComboBox = new JComboBox<>();
        populatePlayerIdComboBox();
        playerPanel.add(playerIdComboBox);

        showPlayerInfoButton = new JButton("Show Player Info");
        playerPanel.add(showPlayerInfoButton);
        showPlayerInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Integer selectedPlayerId = (Integer) playerIdComboBox.getSelectedItem();
                if (selectedPlayerId != null) {
                    Player player = dataOperations.getPlayerInfo(selectedPlayerId);
                    if (player != null) {
                        showPlayerInfoDialog(player);
                    }
                }
            }
        });

        insertPlayerButton = new JButton("Insert Player");
        playerPanel.add(insertPlayerButton);
        clearPlayerButton = new JButton("Clear");
        playerPanel.add(clearPlayerButton);
        updatePlayerButton = new JButton("Update Player");
        playerPanel.add(updatePlayerButton);

        insertPlayerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new PlayerInfoDialog(dataOperations, null, playerIdComboBox).setVisible(true);
            }
        });

        updatePlayerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Integer selectedPlayerId = (Integer) playerIdComboBox.getSelectedItem();
                if (selectedPlayerId != null) {
                    Player player = dataOperations.getPlayerInfo(selectedPlayerId);
                    if (player != null) {
                        new PlayerInfoDialog(dataOperations, player, playerIdComboBox).setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(null, "Select a player to update.");
                    }
                }
            }
        });

        clearPlayerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerIdComboBox.setSelectedItem(null);
            }
        });

        add(playerPanel);
    }

    private void populatePlayerIdComboBox() {
        playerIdComboBox.removeAllItems();
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT player_id FROM player";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                playerIdComboBox.addItem(rs.getInt("player_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showPlayerInfoDialog(Player player) {
        JDialog dialog = new JDialog(this, "Player Information", true);
        dialog.setLayout(new GridLayout(7, 2));
        dialog.setSize(400, 300);

        dialog.add(new JLabel("First Name:"));
        JTextField firstNameField = new JTextField(player.getFirstName());
        firstNameField.setEditable(false);
        dialog.add(firstNameField);

        dialog.add(new JLabel("Last Name:"));
        JTextField lastNameField = new JTextField(player.getLastName());
        lastNameField.setEditable(false);
        dialog.add(lastNameField);

        dialog.add(new JLabel("Address:"));
        JTextField addressField = new JTextField(player.getAddress());
        addressField.setEditable(false);
        dialog.add(addressField);

        dialog.add(new JLabel("Postal Code:"));
        JTextField postalCodeField = new JTextField(player.getPostalCode());
        postalCodeField.setEditable(false);
        dialog.add(postalCodeField);

        dialog.add(new JLabel("Province:"));
        JTextField provinceField = new JTextField(player.getProvince());
        provinceField.setEditable(false);
        dialog.add(provinceField);

        dialog.add(new JLabel("Phone Number:"));
        JTextField phoneNumberField = new JTextField(player.getPhoneNumber());
        phoneNumberField.setEditable(false);
        dialog.add(phoneNumberField);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        dialog.add(closeButton);

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GameApp().setVisible(true);
            }
        });
    }
}
