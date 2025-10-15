/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codeup.libronova.domain;

/**
 *
 * @author Coder
 */
public class User {
    private int idUser;
    private String username;
    private String password;
    private String role; // ADMIN or ASSISTANT
    private String state; // Active or Inactive

    // Constructor
    public User(int idUser, String username, String password, String role, String state, String createdAt, String updatedAt) {
        this.idUser = idUser;
        this.username = username;
        this.password = password;
        this.role = role;
        this.state = state;
    }

    public User(int i, String username, String password, String role, String active, String toString) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    // Getters and Setters (puedes generarlos automáticamente si usas IDE)
    // ... (omitir por brevedad si lo deseas)

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "User{" + "idUser=" + idUser + ", username=" + username + ", password=" + password + ", role=" + role + ", state=" + state + '}';
    }
    
    
}

