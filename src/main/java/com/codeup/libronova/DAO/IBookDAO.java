/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codeup.libronova.DAO;

import com.codeup.libronova.domain.Book;
import com.codeup.libronova.exception.DatabaseException;
import java.util.List;

/**
 *
 * @author Coder
 */
public interface IBookDAO {
    void create(Book book) throws DatabaseException;
    void update(Book book) throws DatabaseException;
    void delete(int idBook) throws DatabaseException;
    void deleteByTitle(String title) throws DatabaseException;
    Book findById(int idBook) throws DatabaseException;
    Book findByIsbn(String isbn) throws DatabaseException;
    List<Book> findByTitle(String title) throws DatabaseException;
    List<Book> findAll() throws DatabaseException;
    List<Book> findByCategory(String category) throws DatabaseException;
    List<Book> findByAuthor(String author) throws DatabaseException;
}



