/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codeup.libronova.DAO;

import com.codeup.libronova.domain.User;
import com.codeup.libronova.exception.DatabaseException;
import java.util.List;

/**
 *
 * @author Coder
 */
public interface IUserDAO {
    
    // Crear usuario
    void addUser(User user) throws DatabaseException;

    // Actualizar usuario
    void updateUser(User user) throws DatabaseException;

    // Eliminar usuario por nombre de usuario
    void deleteUserByUsername(String username) throws DatabaseException;

    // Buscar usuario por nombre de usuario
    User findUserByUsername(String username) throws DatabaseException;

    // Obtener todos los usuarios
    List<User> getAllUsers() throws DatabaseException;

    // Cambiar estado (Activo/Inactivo)
    void changeUserState(String username, String newState) throws DatabaseException;
}
