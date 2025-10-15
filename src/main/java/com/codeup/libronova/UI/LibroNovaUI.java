/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codeup.libronova.UI;

import com.codeup.libronova.DAO.impl.UserDAOimpl;
import com.codeup.libronova.domain.Book;
import com.codeup.libronova.domain.Loan;
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
        showLoginMenu();
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
        String[] options = {"Books", "Members", "Loans", "Users", "Logout"};
        while (true) {
            String choice = (String) JOptionPane.showInputDialog(null, "=== ADMIN MENU ===", "Main Menu",
                    JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
            if (choice == null || choice.equals("Logout")) break;

            switch (choice) {
                case "Books" -> booksMenu();
                case "Members" -> membersMenu();
                case "Loans" -> loansMenu();
                case "Users" -> usersMenu();
            }
        }
    }

    // ====================== MENÚ ASISTENTE ======================
    private void assistantMenu() {
        String[] options = {"Books", "Members", "Loans", "Logout"};
        while (true) {
            String choice = (String) JOptionPane.showInputDialog(null, "=== ASSISTANT MENU ===", "Main Menu",
                    JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
            if (choice == null || choice.equals("Logout")) break;

            switch (choice) {
                case "Books" -> booksMenu();
                case "Members" -> membersMenu();
                case "Loans" -> loansMenu();
            }
        }
    }

    // ====================== LIBROS ======================
    private void booksMenu() {
        String[] options = {"Register Book", "List Books", "Search by Title", "Search by Author", "Search by Category", "Update Book", "Delete Book", "Back"};
        while (true) {
            String choice = (String) JOptionPane.showInputDialog(null, "=== BOOKS MENU ===", "Books",
                    JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
            if (choice == null || choice.equals("Back")) break;

            switch (choice) {
                case "Register Book" -> {
                    String isbn = JOptionPane.showInputDialog("ISBN:");
                    String title = JOptionPane.showInputDialog("Title:");
                    String author = JOptionPane.showInputDialog("Author:");
                    String category = JOptionPane.showInputDialog("Category:");
                    int total = Integer.parseInt(JOptionPane.showInputDialog("Total Copies:"));
                    int available = Integer.parseInt(JOptionPane.showInputDialog("Available Copies:"));
                    double price = Double.parseDouble(JOptionPane.showInputDialog("Price Reference:"));
                    Book book = new Book(isbn, title, author, category, total, available, price, true, LocalDate.now().toString());
                    bookService.createBook(book);
                }
                case "List Books" -> bookService.listAllBooks();
                case "Search by Title" -> {
                    String title = JOptionPane.showInputDialog("Enter title:");
                    bookService.searchByTitle(title);
                }
                case "Search by Author" -> {
                    String author = JOptionPane.showInputDialog("Enter author:");
                    bookService.searchByAuthor(author);
                }
                case "Search by Category" -> {
                    String category = JOptionPane.showInputDialog("Enter category:");
                    bookService.searchByCategory(category);
                }
                case "Update Book" -> {
                    String isbn = JOptionPane.showInputDialog("ISBN of book to update:");
                    String newTitle = JOptionPane.showInputDialog("New title:");
                    String newAuthor = JOptionPane.showInputDialog("New author:");
                    String newCategory = JOptionPane.showInputDialog("New category:");
                    int total = Integer.parseInt(JOptionPane.showInputDialog("Total copies:"));
                    int available = Integer.parseInt(JOptionPane.showInputDialog("Available copies:"));
                    double price = Double.parseDouble(JOptionPane.showInputDialog("Price Reference:"));
                    Book updatedBook = new Book(isbn, newTitle, newAuthor, newCategory, total, available, price, true, LocalDate.now().toString());
                    bookService.updateBook(updatedBook);
                }
                case "Delete Book" -> {
                    String title = JOptionPane.showInputDialog("Enter title to delete:");
                    bookService.deleteBookByTitle(title);
                }
            }
        }
    }

    //MIEMBROS
    private void membersMenu() {
        String[] options = {"Register Member", "List Members", "Search by Name", "Delete Member", "Back"};
        while (true) {
            String choice = (String) JOptionPane.showInputDialog(null, "=== MEMBERS MENU ===", "Members",
                    JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
            if (choice == null || choice.equals("Back")) break;

            switch (choice) {
                case "Register Member" -> {
                    String name = JOptionPane.showInputDialog("Name:");
                    String email = JOptionPane.showInputDialog("Email:");
                    String phone = JOptionPane.showInputDialog("Phone:");
                    Member member = new Member(0, name, email, phone, "Active", LocalDate.now().toString());
                    memberService.addMember(member);
                }
                case "List Members" -> memberService.listAllMembers();
                case "Search by Name" -> {
                    String name = JOptionPane.showInputDialog("Enter name:");
                    memberService.searchMemberByName(name);
                }
                case "Delete Member" -> {
                    String name = JOptionPane.showInputDialog("Enter member name to delete:");
                    memberService.deleteMemberByName(name);
                }
            }
        }
    }

    // PRÉSTAMOS 
    private void loansMenu() {
        String[] options = {"Register Loan", "Return Loan", "List Loans", "Search by Member", "Search by Book", "Back"};
        while (true) {
            String choice = (String) JOptionPane.showInputDialog(null, "=== LOANS MENU ===", "Loans",
                    JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
            if (choice == null || choice.equals("Back")) break;

            switch (choice) {
                case "Register Loan" -> {
                    int memberId = Integer.parseInt(JOptionPane.showInputDialog("Member ID:"));
                    int bookId = Integer.parseInt(JOptionPane.showInputDialog("Book ID:"));
                    Loan loan = new Loan(memberId, bookId, LocalDate.now(), null, 0.0, "BORROWED", LocalDate.now().toString(), null);
                    loanService.registerLoan(loan);
                }
                case "Return Loan" -> {
                    int idLoan = Integer.parseInt(JOptionPane.showInputDialog("Enter Loan ID:"));
                    double penalty = Double.parseDouble(JOptionPane.showInputDialog("Penalty (if any):"));
                    loanService.returnLoan(idLoan, penalty);
                }
                case "List Loans" -> loanService.showAllLoans();
                case "Search by Member" -> {
                    int idMember = Integer.parseInt(JOptionPane.showInputDialog("Member ID:"));
                    loanService.findLoansByMember(idMember);
                }
                case "Search by Book" -> {
                    int idBook = Integer.parseInt(JOptionPane.showInputDialog("Book ID:"));
                    loanService.findLoansByBook(idBook);
                }
            }
        }
    }

    //USUARIOS (solo ADMIN) 
    private void usersMenu() {
        String[] options = {"Add User", "List Users", "Find User", "Delete User", "Change State", "Back"};
        while (true) {
            String choice = (String) JOptionPane.showInputDialog(null, "=== USERS MENU ===", "Users",
                    JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
            if (choice == null || choice.equals("Back")) break;

            switch (choice) {
                case "Add User" -> {
                    String username = JOptionPane.showInputDialog("Username:");
                    String password = JOptionPane.showInputDialog("Password:");
                    String role = JOptionPane.showInputDialog("Role (ADMIN/ASSISTANT):");
                    User user = new User(0, username, password, role, "Active", LocalDate.now().toString());
                    userService.addUser(user);
                }
                case "List Users" -> userService.getAllUsers();
                case "Find User" -> {
                    String username = JOptionPane.showInputDialog("Enter username:");
                    userService.findUserByUsername(username);
                }
                case "Delete User" -> {
                    String username = JOptionPane.showInputDialog("Enter username to delete:");
                    userService.deleteUserByUsername(username);
                }
                case "Change State" -> {
                    String username = JOptionPane.showInputDialog("Username:");
                    String state = JOptionPane.showInputDialog("New state (Active/Inactive):");
                    userService.changeUserState(username, state);
                }
            }
        }
    }
}


