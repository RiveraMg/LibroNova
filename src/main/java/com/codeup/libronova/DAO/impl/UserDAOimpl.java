/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codeup.libronova.DAO.impl;

import com.codeup.libronova.Connection.ConexionDB;
import com.codeup.libronova.DAO.IUserDAO;
import com.codeup.libronova.domain.User;
import com.codeup.libronova.exception.DatabaseException;
import com.codeup.libronova.config.Config;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Coder
 */
public class UserDAOimpl implements IUserDAO {

    private final ConexionDB conexionDB;

    public UserDAOimpl() {
        Config config = new Config();
        this.conexionDB = new ConexionDB(config);
    }

    @Override
    public void addUser(User user) throws DatabaseException {
        String sql = "INSERT INTO Users (username, password, role, state) VALUES (?, ?, ?, ?)";
        try (Connection conn = conexionDB.open();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRole());
            ps.setString(4, user.getState());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error adding new user", e);
        }
    }

    @Override
    public void updateUser(User user) throws DatabaseException {
        String sql = "UPDATE Users SET password = ?, role = ?, state = ? WHERE username = ?";
        try (Connection conn = conexionDB.open();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getPassword());
            ps.setString(2, user.getRole());
            ps.setString(3, user.getState());
            ps.setString(4, user.getUsername());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error updating user information", e);
        }
    }

    @Override
    public void deleteUserByUsername(String username) throws DatabaseException {
        String sql = "DELETE FROM Users WHERE username = ?";
        try (Connection conn = conexionDB.open();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error deleting user", e);
        }
    }

    @Override
    public User findUserByUsername(String username) throws DatabaseException {
        String sql = "SELECT * FROM Users WHERE username = ?";
        try (Connection conn = conexionDB.open();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new User(
                    rs.getInt("id_user"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("role"),
                    rs.getString("state"),
                    rs.getString("created_at"),
                    rs.getString("updated_at")
                );
            }
            return null;
        } catch (SQLException e) {
            throw new DatabaseException("Error finding user by username", e);
        }
    }

    @Override
    public List<User> getAllUsers() throws DatabaseException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM Users";
        try (Connection conn = conexionDB.open();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                users.add(new User(
                    rs.getInt("id_user"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("role"),
                    rs.getString("state"),
                    rs.getString("created_at"),
                    rs.getString("updated_at")
                ));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving all users", e);
        }
        return users;
    }

    @Override
    public void changeUserState(String username, String newState) throws DatabaseException {
        String sql = "UPDATE Users SET state = ? WHERE username = ?";
        try (Connection conn = conexionDB.open();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newState);
            ps.setString(2, username);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error updating user state", e);
        }
    }
}

