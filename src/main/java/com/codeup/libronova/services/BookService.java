/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codeup.libronova.services;

import com.codeup.libronova.DAO.IBookDAO;
import com.codeup.libronova.DAO.impl.BookDAOimpl;
import com.codeup.libronova.config.Config;
import com.codeup.libronova.domain.Book;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Coder
 */

public class BookService {

    private final IBookDAO bookDAO;

    public BookService(IBookDAO bookDAO) {
        this.bookDAO = bookDAO;
    }
    
    public BookService() {
    Config config = new Config(); // inicializas lo que BookDAOimpl necesita
    this.bookDAO = new BookDAOimpl(config);
    }


    // CREATE 
    public void createBook(Book book) {
        try {
            // Validaciones básicas
            if (book.getIsbn() == null || book.getIsbn().isEmpty()) {
                JOptionPane.showMessageDialog(null, "ISBN cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (bookDAO.findByIsbn(book.getIsbn()) != null) {
                JOptionPane.showMessageDialog(null, "ISBN already exists. Please use a unique one.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (book.getTitle() == null || book.getTitle().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Book title cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            bookDAO.create(book);
            JOptionPane.showMessageDialog(null, "Book successfully registered.", "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error registering the book: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // UPDATE
    public void updateBook(Book book) {
        try {
            Book existingBook = bookDAO.findByIsbn(book.getIsbn());
            if (existingBook == null) {
                JOptionPane.showMessageDialog(null, "Book not found with ISBN: " + book.getIsbn(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Asignar el ID del libro existente al objeto que se va a actualizar
            book.setIdBook(existingBook.getIdBook());
            
            bookDAO.update(book);
            JOptionPane.showMessageDialog(null, "Book information successfully updated.", "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error updating the book: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // DELETE BY TITLE
    public void deleteBookByTitle(String title) {
        try {
            List<Book> books = bookDAO.findByTitle(title);
            if (books.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No book found with the title: " + title, "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete the book titled '" + title + "'?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                bookDAO.deleteByTitle(title);
                JOptionPane.showMessageDialog(null, "Book deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error deleting the book: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // LIST ALL 
    public void listAllBooks() {
        try {
            List<Book> books = bookDAO.findAll();
            if (books.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No books found in the catalog.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            StringBuilder sb = new StringBuilder(" BOOK CATALOG \n\n");
            for (Book b : books) {
                String availability = (b.getAvailableCopies() > 0) ? "AVAILABLE" : "NOT AVAILABLE";
                sb.append(String.format(" %s\n   Author: %s\n   Category: %s\n   Availability: %s (%d/%d copies)\n   ISBN: %s\n   Price: $%.2f\n\n",
                        b.getTitle(), b.getAuthor(), b.getCategory(), availability,
                        b.getAvailableCopies(), b.getCopies(), b.getIsbn(), b.getPriceReference()));
            }

            JOptionPane.showMessageDialog(null, sb.toString(), "Book Catalog", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error listing books: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // LIST AVAILABLE BOOKS ONLY
    public void listAvailableBooks() {
        try {
            List<Book> books = bookDAO.findAll();
            List<Book> availableBooks = books.stream()
                .filter(b -> b.getAvailableCopies() > 0)
                .toList();
                
            if (availableBooks.isEmpty()) {
                JOptionPane.showMessageDialog(null, 
                    "No books are available at this time.\n" +
                    "All books are currently borrowed.", 
                    "No Books Available", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            StringBuilder sb = new StringBuilder(" AVAILABLE BOOKS \n\n");
            sb.append("Total available books: ").append(availableBooks.size()).append("\n\n");
            
            for (Book b : availableBooks) {
                sb.append(String.format(" %s\n   Author: %s\n   Category: %s\n   Available copies: %d/%d\n   ISBN: %s\n   Price: $%.2f\n\n",
                        b.getTitle(), b.getAuthor(), b.getCategory(),
                        b.getAvailableCopies(), b.getCopies(), b.getIsbn(), b.getPriceReference()));
            }

            JOptionPane.showMessageDialog(null, sb.toString(), "Available Books", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error listing available books: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // SEARCH BY TITLE 
    public void searchByTitle(String title) {
        try {
            List<Book> books = bookDAO.findByTitle(title);
            showSearchResults(books, "title", title);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error searching books by title: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // SEARCH BY AUTHOR 
    public void searchByAuthor(String author) {
        try {
            List<Book> books = bookDAO.findByAuthor(author);
            showSearchResults(books, "author", author);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error searching books by author: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // SEARCH BY CATEGORY 
    public void searchByCategory(String category) {
        try {
            List<Book> books = bookDAO.findByCategory(category);
            showSearchResults(books, "category", category);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error searching books by category: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //HELPER METHOD 
    private void showSearchResults(List<Book> books, String field, String value) {
        if (books.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No books found with " + field + ": " + value, "No Results", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder("SEARCH RESULTS\n");
        sb.append("Search by " + field + ": " + value + "\n\n");
        
        for (Book b : books) {
            String availability = (b.getAvailableCopies() > 0) ? "AVAILABLE" : "NOT AVAILABLE";
            sb.append(String.format("%s\n   Author: %s\n   Category: %s\n   Availability: %s (%d/%d copies)\n   ISBN: %s\n   Price: $%.2f\n\n",
                    b.getTitle(), b.getAuthor(), b.getCategory(), availability,
                    b.getAvailableCopies(), b.getCopies(), b.getIsbn(), b.getPriceReference()));
        }

        JOptionPane.showMessageDialog(null, sb.toString(), "Search Results", JOptionPane.INFORMATION_MESSAGE);
    }
}


