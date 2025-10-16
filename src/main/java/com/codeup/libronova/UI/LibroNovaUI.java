/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codeup.libronova.UI;

import com.codeup.libronova.DAO.impl.UserDAOimpl;
import com.codeup.libronova.domain.Book;
import com.codeup.libronova.domain.Member;
import com.codeup.libronova.domain.User;
import com.codeup.libronova.services.BookService;
import com.codeup.libronova.services.LoanService;
import com.codeup.libronova.services.MemberService;
import com.codeup.libronova.services.UserService;
import java.time.LocalDate;
import javax.swing.JOptionPane;

/**
 *
 * @author Coder
 */
public class LibroNovaUI {

    private final UserService userService;
    private final BookService bookService;
    private final MemberService memberService;
    private final LoanService loanService;

    public LibroNovaUI() {
        this.userService = new UserService();
        this.bookService = new BookService();
        this.memberService = new MemberService();
        this.loanService = new LoanService();
    }

    public void start() {
        showMainMenu();
    }

    // ====================== HELPER METHODS ======================
    private int showNumberedMenu(String title, String[] options) {
        StringBuilder menu = new StringBuilder(title + "\n\n");
        for (int i = 0; i < options.length; i++) {
            menu.append(String.format("%d. %s\n", i + 1, options[i]));
        }
        menu.append("\nEnter the option number:");
        
        String input = JOptionPane.showInputDialog(null, menu.toString(), "LibroNova", JOptionPane.QUESTION_MESSAGE);
        
        if (input == null) return -1; // Usuario canceló
        
        try {
            int choice = Integer.parseInt(input.trim());
            if (choice >= 1 && choice <= options.length) {
                return choice;
            } else {
                JOptionPane.showMessageDialog(null, "Invalid option. Please enter a number between 1 and " + options.length, 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return showNumberedMenu(title, options); // Recursión para reintentar
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Please enter a valid number", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return showNumberedMenu(title, options); // Recursión para reintentar
        }
    }

    // ====================== MENÚ PRINCIPAL ======================
    private void showMainMenu() {
        while (true) {
            String option = JOptionPane.showInputDialog(
                "WELCOME LIBRONOVA LIBRARY\n\n" +
                "1. View Book Catalog \n" +
                "2. Login \n" +
                "3. Exit\n\n" +
                "Choose an option (1-3):"
            );

            if (option == null || option.equals("3")) {
                JOptionPane.showMessageDialog(null, "Thank you for using LibroNova!");
                return;
            }

            switch (option) {
                case "1":
                    showPublicCatalog();
                    break;
                case "2":
                    showLoginMenu();
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Invalid option, please try again.");
                    break;
            }
        }
    }

    // ====================== CATÁLOGO PÚBLICO ======================
    private void showPublicCatalog() {
        boolean running = true;
        while (running) {
            String option = JOptionPane.showInputDialog(
                "=== PUBLIC BOOK CATALOG ===\n\n" +
                "1. View All Books\n" +
                "2. View Available Books Only\n" +
                "3. Search by Title\n" +
                "4. Search by Author\n" +
                "5. Search by Category\n" +
                "6. Back to Main Menu\n\n" +
                "Choose an option (1-6):"
            );

            if (option == null || option.equals("6")) break;

            try {
                switch (option) {
                    case "1":
                        bookService.listAllBooks();
                        break;
                    case "2":
                        bookService.listAvailableBooks();
                        break;
                    case "3": {
                        String title = JOptionPane.showInputDialog("Enter the title to search:");
                        if (title != null && !title.trim().isEmpty()) {
                            bookService.searchByTitle(title);
                        }
                        break;
                    }
                    case "4": {
                        String author = JOptionPane.showInputDialog("Enter the author to search:");
                        if (author != null && !author.trim().isEmpty()) {
                            bookService.searchByAuthor(author);
                        }
                        break;
                    }
                    case "5": {
                        String category = JOptionPane.showInputDialog("Enter the category to search:");
                        if (category != null && !category.trim().isEmpty()) {
                            bookService.searchByCategory(category);
                        }
                        break;
                    }
                    default:
                        JOptionPane.showMessageDialog(null, "Invalid option, please try again.");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ====================== LOGIN ======================
    private void showLoginMenu() {
        while (true) {
            String username = JOptionPane.showInputDialog("LOGIN \nEnter username:");
            if (username == null) return;

            String password = JOptionPane.showInputDialog("Enter password:");
            if (password == null) return;

            try {
                User user = new UserDAOimpl().findUserByUsername(username);

                if (user == null || !user.getPassword().equals(password)) {
                    JOptionPane.showMessageDialog(null, "Invalid credentials.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                    continue;
                }

                if (user.getState().equalsIgnoreCase("Inactive")) {
                    JOptionPane.showMessageDialog(null, "User inactive.", "Access Denied", JOptionPane.WARNING_MESSAGE);
                    continue;
                }

                JOptionPane.showMessageDialog(null, "Welcome " + user.getUsername() + " (" + user.getRole() + ")");
                System.out.println("[HTTP POST] /login → user=" + user.getUsername() + ", role=" + user.getRole());

                if (user.getRole().equalsIgnoreCase("ADMIN")) {
                    adminMenu();
                } else {
                    assistantMenu();
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ====================== MENÚ ADMIN ======================
    private void adminMenu() {
        boolean running = true;
        while (running) {
            String option = JOptionPane.showInputDialog(
                "ADMINISTRATOR MENU \n\n" +
                "1. Manage Books\n" +
                "2. Manage Members\n" +
                "3. Manage Loans\n" +
                "4. Manage Users\n" +
                "5. Log Out\n\n" +
                "Choose an option (1-5):"
            );

            if (option == null || option.equals("5")) break;

            switch (option) {
                case "1":
                    booksMenu();
                    break;
                case "2":
                    membersMenu();
                    break;
                case "3":
                    loansMenu();
                    break;
                case "4":
                    usersMenu();
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Invalid option, please try again.");
                    break;
            }
        }
    }

    // ====================== MENÚ ASISTENTE ======================
    private void assistantMenu() {
        boolean running = true;
        while (running) {
            String option = JOptionPane.showInputDialog(
                "ASSISTANT MENU \n\n" +
                "1. Manage Books\n" +
                "2. Manage Members\n" +
                "3. Manage Loans\n" +
                "4. Log Out\n\n" +
                "Choose an option (1-4):"
            );

            if (option == null || option.equals("4")) break;

            switch (option) {
                case "1":
                    booksMenu();
                    break;
                case "2":
                    membersMenu();
                    break;
                case "3":
                    loansMenu();
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Invalid option, please try again.");
                    break;
            }
        }
    }

    // ====================== LIBROS ======================
    private void booksMenu() {
        boolean running = true;
        while (running) {
            String option = JOptionPane.showInputDialog(
                "BOOK MANAGEMENT \n\n" +
                "1. Register Book\n" +
                "2. List All Books\n" +
                "3. Search by Title\n" +
                "4. Search by Author\n" +
                "5. Search by Category\n" +
                "6. Update Book\n" +
                "7. Delete Book\n" +
                "8. Return to Main Menu\n\n" +
                "Choose an option (1-8):"
            );

            if (option == null || option.equals("8")) break;

            try {
                switch (option) {
                    case "1": {
                        String isbn = JOptionPane.showInputDialog("Enter the ISBN of the book:");
                        String title = JOptionPane.showInputDialog("Enter the title of the book:");
                        String author = JOptionPane.showInputDialog("Enter the author of the book:");
                        String category = JOptionPane.showInputDialog("Enter the book category:");
                        int total = Integer.parseInt(JOptionPane.showInputDialog("Enter the total number of copies:"));
                        int available = Integer.parseInt(JOptionPane.showInputDialog("Enter the number of copies available:"));
                        double price = Double.parseDouble(JOptionPane.showInputDialog("Enter the reference price:"));
                        Book book = new Book(isbn, title, author, category, total, available, price, true, LocalDate.now().toString());
                        bookService.createBook(book);
                        break;
                    }
                    case "2":
                        bookService.listAllBooks();
                        break;
                    case "3": {
                        String title = JOptionPane.showInputDialog("Enter the title to search:");
                        bookService.searchByTitle(title);
                        break;
                    }
                    case "4": {
                        String author = JOptionPane.showInputDialog("Enter the author to search:");
                        bookService.searchByAuthor(author);
                        break;
                    }
                    case "5": {
                        String category = JOptionPane.showInputDialog("Enter the category to search:");
                        bookService.searchByCategory(category);
                        break;
                    }
                    case "6": {
                        String isbn = JOptionPane.showInputDialog("Enter the ISBN of the book to be updated:");
                        String newTitle = JOptionPane.showInputDialog("Enter the new title:");
                        String newAuthor = JOptionPane.showInputDialog("Enter the new author:");
                        String newCategory = JOptionPane.showInputDialog("Enter the new category:");
                        int total = Integer.parseInt(JOptionPane.showInputDialog("Enter the total number of copies:"));
                        int available = Integer.parseInt(JOptionPane.showInputDialog("Enter the number of copies available:"));
                        double price = Double.parseDouble(JOptionPane.showInputDialog("Enter the reference price:"));
                        Book updatedBook = new Book(isbn, newTitle, newAuthor, newCategory, total, available, price, true, LocalDate.now().toString());
                        bookService.updateBook(updatedBook);
                        break;
                    }
                    case "7": {
                        String title = JOptionPane.showInputDialog("Enter the title of the book to delete: ");
                        bookService.deleteBookByTitle(title);
                        break;
                    }
                    default:
                        JOptionPane.showMessageDialog(null, "Invalid option, please try again.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Error: Please enter a valid number");
            }
        }
    }

    //MIEMBROS
    private void membersMenu() {
        boolean running = true;
        while (running) {
            String option = JOptionPane.showInputDialog(
                "MEMBER MANAGEMENT \n\n" +
                "1. Register Member\n" +
                "2. List All Members\n" +
                "3. Search by Name\n" +
                "4. Delete Member\n" +
                "5. Return to Main Menu\n\n" +
                "Choose an option (1-5):"
            );

            if (option == null || option.equals("5")) break;

            try {
                switch (option) {
                    case "1": {
                        String name = JOptionPane.showInputDialog("Enter the member's name:");
                        String email = JOptionPane.showInputDialog("Enter the member's email:");
                        String phone = JOptionPane.showInputDialog("Enter the member's phone number:");
                        Member member = new Member(0, name, email, phone, "Active", LocalDate.now().toString());
                        memberService.addMember(member);
                        break;
                    }
                    case "2":
                        memberService.listAllMembers();
                        break;
                    case "3": {
                        String name = JOptionPane.showInputDialog("Enter the name to search for:");
                        memberService.searchMemberByName(name);
                        break;
                    }
                    case "4": {
                        String name = JOptionPane.showInputDialog("Enter the name of the member to be removed:");
                        memberService.deleteMemberByName(name);
                        break;
                    }
                    default:
                        JOptionPane.showMessageDialog(null, "Invalid option, please try again.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Error: Please enter a valid number");
            }
        }
    }

    // PRÉSTAMOS 
    private void loansMenu() {
        boolean running = true;
        while (running) {
            String option = JOptionPane.showInputDialog(
                "LOAN MANAGEMENT \n\n" +
                "1. Register Loan\n" +
                "2. Return Loan\n" +
                "3. List All Loans\n" +
                "4. Search by Member Name\n" +
                "5. Search by Book Title\n" +
                "6. View Members and Available Books\n" +
                "7. Test Database Connection\n" +
                "8. Return to Main Menu\n\n" +
                "Choose an option (1-8):"
            );

            if (option == null || option.equals("8")) break;

            try {
                switch (option) {
                    case "1": {
                        String memberName = JOptionPane.showInputDialog("Enter the member's name:");
                        if (memberName == null || memberName.trim().isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Member name is required", "Error", JOptionPane.ERROR_MESSAGE);
                            break;
                        }
                        
                        String bookTitle = JOptionPane.showInputDialog("Enter the title of the book:");
                        if (bookTitle == null || bookTitle.trim().isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Book title is required", "Error", JOptionPane.ERROR_MESSAGE);
                            break;
                        }
                        
                        loanService.registerLoanByNames(memberName.trim(), bookTitle.trim());
                        break;
                    }
                    case "2": {
                        String memberName = JOptionPane.showInputDialog("Enter the name of the member returning the book:");
                        if (memberName == null || memberName.trim().isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Member name is required", "Error", JOptionPane.ERROR_MESSAGE);
                            break;
                        }
                        
                        String bookTitle = JOptionPane.showInputDialog("Enter the title of the book to be returned:");
                        if (bookTitle == null || bookTitle.trim().isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Book title is required", "Error", JOptionPane.ERROR_MESSAGE);
                            break;
                        }
                        
                        loanService.returnLoanByNames(memberName.trim(), bookTitle.trim());
                        break;
                    }
                    case "3":
                        loanService.showAllLoans();
                        break;
                    case "4": {
                        String memberName = JOptionPane.showInputDialog("Enter the member's name:");
                        if (memberName != null && !memberName.trim().isEmpty()) {
                            loanService.findLoansByMemberName(memberName);
                        }
                        break;
                    }
                    case "5": {
                        String bookTitle = JOptionPane.showInputDialog("Enter the title of the book:");
                        if (bookTitle != null && !bookTitle.trim().isEmpty()) {
                            loanService.findLoansByBookTitle(bookTitle);
                        }
                        break;
                    }
                    case "6":
                        loanService.showAvailableMembersAndBooks();
                        break;
                    case "7":
                        loanService.testDatabaseConnection();
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Invalid option, please try again.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Error: Please enter a valid number");
            }
        }
    }

    //USUARIOS (solo ADMIN) 
    private void usersMenu() {
        boolean running = true;
        while (running) {
            String option = JOptionPane.showInputDialog(
                "USER MANAGEMENT\n\n" +
                "1. Add User\n" +
                "2. List All Users\n" +
                "3. Search User\n" +
                "4. Delete User\n" +
                "5. Change User Status\n" +
                "6. Return to Main Menu\n\n" +
                "Choose an option (1-6):"
            );

            if (option == null || option.equals("6")) break;

            switch (option) {
                case "1": {
                    String username = JOptionPane.showInputDialog("Enter your username:");
                    String password = JOptionPane.showInputDialog("Enter the password:");
                    String role = JOptionPane.showInputDialog("Enter the role (ADMIN/ASSISTANT):");
                    User user = new User(0, username, password, role, "Active", LocalDate.now().toString());
                    userService.addUser(user);
                    break;
                }
                case "2":
                    userService.getAllUsers();
                    break;
                case "3": {
                    String username = JOptionPane.showInputDialog("Enter the username to search:");
                    userService.findUserByUsername(username);
                    break;
                }
                case "4": {
                    String username = JOptionPane.showInputDialog("Enter the username to delete:");
                    userService.deleteUserByUsername(username);
                    break;
                }
                case "5": {
                    String username = JOptionPane.showInputDialog("Enter the username:");
                    String state = JOptionPane.showInputDialog("Enter the new status (Active/Inactive):");
                    userService.changeUserState(username, state);
                    break;
                }
                default: 
                    JOptionPane.showMessageDialog(null, "Invalid option, please try again");
                    break;
            }
        }
    }
}


