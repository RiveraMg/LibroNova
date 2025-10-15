/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codeup.libronova.Connection;

import com.codeup.libronova.config.Config;
import com.codeup.libronova.exception.DatabaseException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Coder
 */
public class ConexionDB {
    private final Config cfg;

    public ConexionDB(Config cfg) {
        this.cfg = cfg;
    }

    public Connection open() throws DatabaseException {
        String url = cfg.get("db.url");
        String user = cfg.get("db.user");
        String pass = cfg.get("db.password");
        
        try {
            // Registrar el driver (opcional si usas MySQL 8 o superior)
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Intentar abrir la conexión
            return DriverManager.getConnection(url, user, pass);
        } catch (SQLException e) {
            throw new DatabaseException("Error connecting to the database", e);
        } catch (ClassNotFoundException e) {
            throw new DatabaseException("MySQL Driver Not Found", e);
        }
    }

}

