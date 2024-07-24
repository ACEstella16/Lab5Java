package com.example.lab5;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerInfoDialog extends JDialog {
    public JTextField firstNameField;
    public JTextField lastNameField;
    public JTextField addressField;
    public JTextField postalCodeField;
    public JTextField provinceField;
    public JTextField phoneNumberField;
    public JButton saveButton;
    public DataOperations dataOperations;
    public Player player;
    public JComboBox<Integer> playerIdComboBox;

    public PlayerInfoDialog(DataOperations dataOperations, Player player, JComboBox<Integer> playerIdComboBox) {
        this.dataOperations = dataOperations;
        this.player = player;
        this.playerIdComboBox = playerIdComboBox;

        setTitle(player == null ? "Insert Player" : "Update Player");
        setLayout(new GridLayout(8, 2));
        setSize(400, 400);

        add(new JLabel("First Name:"));
        firstNameField = new JTextField(player == null ? "" : player.getFirstName());
        add(firstNameField);

        add(new JLabel("Last Name:"));
        lastNameField = new JTextField(player == null ? "" : player.getLastName());
        add(lastNameField);

        add(new JLabel("Address:"));
        addressField = new JTextField(player == null ? "" : player.getAddress());
        add(addressField);

        add(new JLabel("Postal Code:"));
        postalCodeField = new JTextField(player == null ? "" : player.getPostalCode());
        add(postalCodeField);

        add(new JLabel("Province:"));
        provinceField = new JTextField(player == null ? "" : player.getProvince());
        add(provinceField);

        add(new JLabel("Phone Number:"));
        phoneNumberField = new JTextField(player == null ? "" : player.getPhoneNumber());
        add(phoneNumberField);

        saveButton = new JButton(player == null ? "Insert" : "Update");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String firstName = firstNameField.getText();
                String lastName = lastNameField.getText();
                String address = addressField.getText();
                String postalCode = postalCodeField.getText();
                String province = provinceField.getText();
                String phoneNumber = phoneNumberField.getText();
                try {
                    if (player == null) {
                        dataOperations.insertPlayer(firstName, lastName, address, postalCode, province, phoneNumber);
                        JOptionPane.showMessageDialog(null, "Player inserted successfully!");
                    } else {
                        dataOperations.updatePlayer(player.getPlayerId(), firstName, lastName, address, postalCode, province, phoneNumber);
                        JOptionPane.showMessageDialog(null, "Player updated successfully!");
                    }
                    playerIdComboBox.removeAllItems();
                    populatePlayerIdComboBox();
                    dispose();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        add(saveButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        add(cancelButton);

        setLocationRelativeTo(null);
    }

    private void populatePlayerIdComboBox() {
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
}
