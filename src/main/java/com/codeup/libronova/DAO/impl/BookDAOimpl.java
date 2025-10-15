/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codeup.libronova.DAO.impl;

import com.codeup.libronova.Connection.ConexionDB;
import com.codeup.libronova.DAO.IBookDAO;
import com.codeup.libronova.config.Config;
import com.codeup.libronova.domain.Book;
import com.codeup.libronova.exception.DatabaseException;
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

public class BookDAOimpl implements IBookDAO {

    private final ConexionDB conexionDB;

    public BookDAOimpl(Config config) {
        this.conexionDB = new ConexionDB(config);
    }

    @Override
    public void create(Book book) throws DatabaseException {
        String sql = "INSERT INTO Books (isbn, title, author, category, copies, availableCopies, priceReference, isActivo, createdAt, updatedAt) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())";
        try (Connection conn = conexionDB.open();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, book.getIsbn());
            ps.setString(2, book.getTitle());
            ps.setString(3, book.getAuthor());
            ps.setString(4, book.getCategory());
            ps.setInt(5, book.getCopies());
            ps.setInt(6, book.getAvailableCopies());
            ps.setDouble(7, book.getPriceReference());
            ps.setBoolean(8, book.isIsActivo());
            
            ps.executeUpdate();
            
        } catch (SQLException e) {
            throw new DatabaseException("Error inserting book", e);
        }
    }

    @Override
    public void update(Book book) throws DatabaseException {
        String sql = "UPDATE Books SET isbn=?, title=?, author=?, category=?, copies=?, availableCopies=?, priceReference=?, isActivo=?, updatedAt=NOW() "
                   + "WHERE id_book = ?";
        try (Connection conn = conexionDB.open();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, book.getIsbn());
            ps.setString(2, book.getTitle());
            ps.setString(3, book.getAuthor());
            ps.setString(4, book.getCategory());
            ps.setInt(5, book.getCopies());
            ps.setInt(6, book.getAvailableCopies());
            ps.setDouble(7, book.getPriceReference());
            ps.setBoolean(8, book.isIsActivo());
            ps.setInt(9, book.getIdBook());
            
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error updating book", e);
        }
    }

    @Override
    public void delete(int idBook) throws DatabaseException {
        String sql = "DELETE FROM Books WHERE id_book = ?";
        try (Connection conn = conexionDB.open();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idBook);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error deleting book", e);
        }
    }
    
    @Override
    public void deleteByTitle(String title) throws DatabaseException {
        String sql = "DELETE FROM Books WHERE title = ?";
        try (Connection conn = conexionDB.open();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, title);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error deleting book by title", e);
        }
    }

    @Override
    public Book findById(int idBook) throws DatabaseException {
        String sql = "SELECT * FROM Books WHERE id_book = ?";
        try (Connection conn = conexionDB.open();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idBook);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) return mapBook(rs);
        } catch (SQLException e) {
            throw new DatabaseException("Error finding book by ID", e);
        }
        return null;
    }

    @Override
    public Book findByIsbn(String isbn) throws DatabaseException {
        String sql = "SELECT * FROM books WHERE isbn = ?";
        try (Connection conn = conexionDB.open();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, isbn);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) return mapBook(rs);
        } catch (SQLException e) {
            throw new DatabaseException("Error finding book by ISBN", e);
        }
        return null;
    }
    
        @Override
    public List<Book> findByTitle(String title) throws DatabaseException {
        List<Book> list = new ArrayList<>();
        String sql = "SELECT * FROM Books WHERE title = ?";
        try (Connection conn = conexionDB.open();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, title);
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapBook(rs));
        } catch (SQLException e) {
            throw new DatabaseException("Error finding book by title", e);
        }
        return null;
    }

    @Override
    public List<Book> findAll() throws DatabaseException {
        List<Book> list = new ArrayList<>();
        String sql = "SELECT * FROM Books";
        try (Connection conn = conexionDB.open();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) list.add(mapBook(rs));
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching all books", e);
        }
        return list;
    }

    @Override
    public List<Book> findByCategory(String category) throws DatabaseException {
        List<Book> list = new ArrayList<>();
        String sql = "SELECT * FROM Books WHERE category = ?";
        try (Connection conn = conexionDB.open();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, category);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapBook(rs));
        } catch (SQLException e) {
            throw new DatabaseException("Error finding books by category", e);
        }
        return list;
    }

    @Override
    public List<Book> findByAuthor(String author) throws DatabaseException {
        List<Book> list = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE author=?";
        try (Connection conn = conexionDB.open();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, author);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapBook(rs));
        } catch (SQLException e) {
            throw new DatabaseException("Error finding books by author", e);
        }
        return list;
    }

    // ======================== HELPER ===========================
    private Book mapBook(ResultSet rs) throws SQLException {
        Book b = new Book(
                rs.getString("isbn"),
                rs.getString("title"),
                rs.getString("author"),
                rs.getString("category"),
                rs.getInt("copies"),
                rs.getInt("availableCopies"),
                rs.getDouble("priceReference"),
                rs.getBoolean("isActivo")
        );
        b.setIdBook(rs.getInt("idBook"));
        b.setCreatedAt(rs.getString("createdAt"));
        b.setUpdatedAt(rs.getString("updatedAt"));
        return b;
    }
}




