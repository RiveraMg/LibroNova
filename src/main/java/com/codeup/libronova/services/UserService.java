/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codeup.libronova.services;

import com.codeup.libronova.DAO.IUserDAO;
import com.codeup.libronova.domain.User;
import com.codeup.libronova.DAO.impl.UserDAOimpl;
import com.codeup.libronova.exception.DatabaseException;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Coder
 */
public class UserService {

    private IUserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAOimpl();
    }

    // CREATE 
    public void addUser(User user) {
        try {
            if (user.getUsername() == null || user.getUsername().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Username cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Verificar si el usuario ya existe
            User existingUser = userDAO.findUserByUsername(user.getUsername());
            if (existingUser != null) {
                JOptionPane.showMessageDialog(null, "Username already exists!", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            userDAO.addUser(user);
            JOptionPane.showMessageDialog(null, "User created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (DatabaseException e) {
            JOptionPane.showMessageDialog(null, "Error creating user: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // UPDATE 
    public void updateUser(User user) {
        try {
            userDAO.updateUser(user);
            JOptionPane.showMessageDialog(null, "User updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (DatabaseException e) {
            JOptionPane.showMessageDialog(null, "Error updating user: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // DELETE 
    public void deleteUserByUsername(String username) {
        try {
            User user = userDAO.findUserByUsername(username);
            if (user == null) {
                JOptionPane.showMessageDialog(null, "No user found with username: " + username, "Not Found", JOptionPane.WARNING_MESSAGE);
                return;
            }

            userDAO.deleteUserByUsername(username);
            JOptionPane.showMessageDialog(null, "User deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (DatabaseException e) {
            JOptionPane.showMessageDialog(null, "Error deleting user: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // FIND BY USERNAME 
    public void findUserByUsername(String username) {
        try {
            User user = userDAO.findUserByUsername(username);
            if (user == null) {
                JOptionPane.showMessageDialog(null, "No user found with username: " + username, "Not Found", JOptionPane.WARNING_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(null, user.toString(), "User Details", JOptionPane.INFORMATION_MESSAGE);
        } catch (DatabaseException e) {
            JOptionPane.showMessageDialog(null, "Error finding user: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // LIST ALL 
    public void getAllUsers() {
        try {
            List<User> users = userDAO.getAllUsers();
            if (users.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No users found.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            StringBuilder sb = new StringBuilder("Users List\n\n");
            for (User u : users) {
                sb.append(String.format("ID: %d | Username: %s | Role: %s | State: %s\n",
                        u.getIdUser(), u.getUsername(), u.getRole(), u.getState()));
            }

            JOptionPane.showMessageDialog(null, sb.toString(), "All Users", JOptionPane.INFORMATION_MESSAGE);

        } catch (DatabaseException e) {
            JOptionPane.showMessageDialog(null, "Error listing users: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // CHANGE STATE
    public void changeUserState(String username, String newState) {
        try {
            User user = userDAO.findUserByUsername(username);
            if (user == null) {
                JOptionPane.showMessageDialog(null, "No user found with username: " + username, "Not Found", JOptionPane.WARNING_MESSAGE);
                return;
            }

            userDAO.changeUserState(username, newState);
            JOptionPane.showMessageDialog(null, "User state changed to: " + newState, "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (DatabaseException e) {
            JOptionPane.showMessageDialog(null, "Error changing user state: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //LOGIN
    public void login(String username, String password) {
        try {
            User user = userDAO.findUserByUsername(username);

            if (user == null || !user.getPassword().equals(password)) {
                JOptionPane.showMessageDialog(null, "Invalid username or password.", "Authentication Failed", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (user.getState().equalsIgnoreCase("Inactive")) {
                JOptionPane.showMessageDialog(null, "User is inactive.", "Access Denied", JOptionPane.WARNING_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(null,
                    "Login successful! Welcome, " + user.getUsername() + " (" + user.getRole() + ")",
                    "Success", JOptionPane.INFORMATION_MESSAGE);

            System.out.println("[HTTP POST] /login → user=" + user.getUsername() + ", role=" + user.getRole());

        } catch (DatabaseException e) {
            JOptionPane.showMessageDialog(null, "Error during login: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
