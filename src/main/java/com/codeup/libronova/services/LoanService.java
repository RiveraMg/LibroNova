/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codeup.libronova.services;

import com.codeup.libronova.DAO.ILoanDAO;
import com.codeup.libronova.DAO.impl.LoanDAOimpl;
import com.codeup.libronova.DAO.impl.MemberDAOimpl;
import com.codeup.libronova.DAO.impl.BookDAOimpl;
import com.codeup.libronova.domain.Loan;
import com.codeup.libronova.domain.Member;
import com.codeup.libronova.domain.Book;
import com.codeup.libronova.exception.DatabaseException;
import com.codeup.libronova.config.Config;
import java.time.LocalDate;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Coder
 */
public class LoanService {
    private final ILoanDAO loanDAO;
    private final MemberDAOimpl memberDAO;
    private final BookDAOimpl bookDAO;

    public LoanService() {
        Config config = new Config();
        this.loanDAO = new LoanDAOimpl();
        this.memberDAO = new MemberDAOimpl();
        this.bookDAO = new BookDAOimpl(config);
    }

    // REGISTER LOAN
    public void registerLoan(Loan loan) {
        try {
            if (loan == null) {
                JOptionPane.showMessageDialog(null, "Loan data cannot be empty.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (loan.getIdBook() <= 0 || loan.getIdMember() <= 0) {
                JOptionPane.showMessageDialog(null, "Invalid member or book ID.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Verificar que el libro existe y tiene copias disponibles
            Book book = bookDAO.findById(loan.getIdBook());
            if (book == null) {
                JOptionPane.showMessageDialog(null, "Book not found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (book.getAvailableCopies() <= 0) {
                JOptionPane.showMessageDialog(null, "No copies available for this book.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Verificar que el miembro existe
            Member member = memberDAO.getMemberById(loan.getIdMember());
            if (member == null) {
                JOptionPane.showMessageDialog(null, "Member not found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (loan.getLoanDatetime() == null) {
                loan.setLoanDatetime(LocalDate.now());
            }

            loan.setState("BORROWED");
            loanDAO.addLoan(loan);

            // Reducir las copias disponibles del libro
            book.setAvailableCopies(book.getAvailableCopies() - 1);
            bookDAO.update(book);

            JOptionPane.showMessageDialog(null, "Loan successfully registered!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (DatabaseException e) {
            JOptionPane.showMessageDialog(null, "Error registering loan: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // RETURN BOOK
    public void returnLoan(int idLoan, double penalty) {
        try {
            Loan loan = loanDAO.findLoanById(idLoan);
            if (loan == null) {
                JOptionPane.showMessageDialog(null, "Loan not found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if ("REPAY".equalsIgnoreCase(loan.getState())) {
                JOptionPane.showMessageDialog(null, "This loan has already been returned.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            loan.setRefundDatetime(LocalDate.now());
            loan.setPenalty(penalty);
            loan.setState("REPAY");
            loanDAO.updateLoan(loan);

            // Incrementar las copias disponibles del libro
            Book book = bookDAO.findById(loan.getIdBook());
            if (book != null) {
                book.setAvailableCopies(book.getAvailableCopies() + 1);
                bookDAO.update(book);
            }

            JOptionPane.showMessageDialog(null, "Book successfully returned!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (DatabaseException e) {
            JOptionPane.showMessageDialog(null, "Error returning loan: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // UPDATE LOAN
    public void updateLoan(Loan loan) {
        try {
            loanDAO.updateLoan(loan);
            JOptionPane.showMessageDialog(null, "Loan updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (DatabaseException e) {
            JOptionPane.showMessageDialog(null, "Error updating loan: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // DELETE LOAN
    public void deleteLoan(int idLoan) {
        try {
            loanDAO.deleteLoan(idLoan);
            JOptionPane.showMessageDialog(null, "Loan deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (DatabaseException e) {
            JOptionPane.showMessageDialog(null, "Error deleting loan: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //SEARCH LOAN BY ID
    public void searchLoanById(int idLoan) {
        try {
            Loan loan = loanDAO.findLoanById(idLoan);
            if (loan != null) {
                JOptionPane.showMessageDialog(null, loan.toString(), "Loan Found", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "No loan found with ID: " + idLoan, "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (DatabaseException e) {
            JOptionPane.showMessageDialog(null, "Error searching loan: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // GET ALL LOANS
    public void showAllLoans() {
        try {
            List<Loan> loans = loanDAO.getAllLoans();
            if (loans.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No loans found in the system.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            StringBuilder sb = new StringBuilder("ALL LOANS\n\n");
            for (Loan l : loans) {
                sb.append(formatLoanWithNames(l)).append("\n");
            }

            JOptionPane.showMessageDialog(null, sb.toString(), "Loans List", JOptionPane.INFORMATION_MESSAGE);
        } catch (DatabaseException e) {
            JOptionPane.showMessageDialog(null, "Error retrieving loans: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // FIND LOANS BY MEMBER
    public void findLoansByMember(int idMember) {
        try {
            List<Loan> loans = loanDAO.findLoansByMember(idMember);
            if (loans.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No loans found for this member.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            StringBuilder sb = new StringBuilder("Loans for Member ID: " + idMember + "\n\n");
            for (Loan l : loans) {
                sb.append(l.toString()).append("\n");
            }

            JOptionPane.showMessageDialog(null, sb.toString(), "Member Loans", JOptionPane.INFORMATION_MESSAGE);
        } catch (DatabaseException e) {
            JOptionPane.showMessageDialog(null, "Error retrieving loans: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // FIND LOANS BY BOOK 
    public void findLoansByBook(int idBook) {
        try {
            List<Loan> loans = loanDAO.findLoansByBook(idBook);
            if (loans.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No loans found for this book.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            StringBuilder sb = new StringBuilder("Loans for Book ID: " + idBook + "\n\n");
            for (Loan l : loans) {
                sb.append(l.toString()).append("\n");
            }

            JOptionPane.showMessageDialog(null, sb.toString(), "Book Loans", JOptionPane.INFORMATION_MESSAGE);
        } catch (DatabaseException e) {
            JOptionPane.showMessageDialog(null, "Error retrieving loans: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // FIND LOANS BY STATE 
    public void findLoansByState(String state) {
        try {
            List<Loan> loans = loanDAO.findLoansByState(state);
            if (loans.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No loans found with state: " + state, "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            StringBuilder sb = new StringBuilder("Loans with state: " + state + "\n\n");
            for (Loan l : loans) {
                sb.append(l.toString()).append("\n");
            }

            JOptionPane.showMessageDialog(null, sb.toString(), "Loans by State", JOptionPane.INFORMATION_MESSAGE);
        } catch (DatabaseException e) {
            JOptionPane.showMessageDialog(null, "Error retrieving loans: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ===================== HELPER METHODS ==========================
    private String formatLoanWithNames(Loan loan) {
        try {
            Member member = memberDAO.getMemberById(loan.getIdMember());
            Book book = bookDAO.findById(loan.getIdBook());
            
            String memberName = (member != null) ? member.getName() : "Unknown Member";
            String bookTitle = (book != null) ? book.getTitle() : "Unknown Book";
            
            String refundDate = (loan.getRefundDatetime() != null) ? 
                loan.getRefundDatetime().toString() : "Not returned";
            
            // Calcular fecha de devolución esperada
            LocalDate expectedReturn = loan.getLoanDatetime().plusDays(7);
            String expectedReturnStr = expectedReturn.toString();
            
            return String.format("ID: %d | Member: %s | Book: %s | Loan: %s | Expected: %s | Return: %s | Penalty: $%.2f | State: %s",
                loan.getIdLoan(), memberName, bookTitle, 
                loan.getLoanDatetime().toString(), expectedReturnStr, refundDate, 
                loan.getPenalty(), loan.getState());
        } catch (DatabaseException e) {
            return String.format("ID: %d | Member ID: %d | Book ID: %d | Loan Date: %s | Return Date: %s | Penalty: $%.2f | State: %s",
                loan.getIdLoan(), loan.getIdMember(), loan.getIdBook(),
                loan.getLoanDatetime().toString(), 
                (loan.getRefundDatetime() != null) ? loan.getRefundDatetime().toString() : "Not returned",
                loan.getPenalty(), loan.getState());
        }
    }

    // FIND LOANS BY MEMBER NAME
    public void findLoansByMemberName(String memberName) {
        try {
            List<Member> members = memberDAO.getAllMembers();
            List<Member> matchingMembers = members.stream()
                .filter(m -> m.getName().toLowerCase().contains(memberName.toLowerCase()))
                .toList();
            
            if (matchingMembers.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No members found with name containing: " + memberName, "Not Found", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            StringBuilder sb = new StringBuilder("=== LOANS BY MEMBER NAME ===\n\n");
            for (Member member : matchingMembers) {
                List<Loan> loans = loanDAO.findLoansByMember(member.getIdMember());
                if (!loans.isEmpty()) {
                    sb.append("Member: ").append(member.getName()).append("\n");
                    for (Loan loan : loans) {
                        sb.append("  ").append(formatLoanWithNames(loan)).append("\n");
                    }
                    sb.append("\n");
                }
            }
            
            if (sb.toString().equals("LOANS BY MEMBER NAME\n\n")) {
                JOptionPane.showMessageDialog(null, "No loans found for members with name containing: " + memberName, "Not Found", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, sb.toString(), "Loans by Member Name", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (DatabaseException e) {
            JOptionPane.showMessageDialog(null, "Error searching loans by member name: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // FIND LOANS BY BOOK TITLE
    public void findLoansByBookTitle(String bookTitle) {
        try {
            List<Book> books = bookDAO.findAll();
            List<Book> matchingBooks = books.stream()
                .filter(b -> b.getTitle().toLowerCase().contains(bookTitle.toLowerCase()))
                .toList();
            
            if (matchingBooks.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No books found with title containing: " + bookTitle, "Not Found", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            StringBuilder sb = new StringBuilder("LOANS BY BOOK TITLE\n\n");
            for (Book book : matchingBooks) {
                List<Loan> loans = loanDAO.findLoansByBook(book.getIdBook());
                if (!loans.isEmpty()) {
                    sb.append("Book: ").append(book.getTitle()).append("\n");
                    for (Loan loan : loans) {
                        sb.append("  ").append(formatLoanWithNames(loan)).append("\n");
                    }
                    sb.append("\n");
                }
            }
            
            if (sb.toString().equals("LOANS BY BOOK TITLE\n\n")) {
                JOptionPane.showMessageDialog(null, "No loans found for books with title containing: " + bookTitle, "Not Found", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, sb.toString(), "Loans by Book Title", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (DatabaseException e) {
            JOptionPane.showMessageDialog(null, "Error searching loans by book title: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // REGISTER LOAN BY NAMES
    public void registerLoanByNames(String memberName, String bookTitle) {
        try {
            // Buscar miembro por nombre
            List<Member> members = memberDAO.getAllMembers();
            Member member = members.stream()
                .filter(m -> m.getName().equalsIgnoreCase(memberName))
                .findFirst()
                .orElse(null);
            
            if (member == null) {
                // Mostrar lista de miembros disponibles
                StringBuilder memberList = new StringBuilder("Member not found. Available members:\n\n");
                for (Member m : members) {
                    memberList.append("- ").append(m.getName()).append("\n");
                }
                JOptionPane.showMessageDialog(null, memberList.toString(), "Member Not Found", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Buscar libro por título
            List<Book> books = bookDAO.findAll();
            Book book = books.stream()
                .filter(b -> b.getTitle().equalsIgnoreCase(bookTitle))
                .findFirst()
                .orElse(null);
            
            if (book == null) {
                // Mostrar lista de libros disponibles
                StringBuilder bookList = new StringBuilder("Book not found. Available books:\n\n");
                for (Book b : books) {
                    if (b.getAvailableCopies() > 0) {
                        bookList.append("- ").append(b.getTitle()).append(" (Available copies: ").append(b.getAvailableCopies()).append(")\n");
                    }
                }
                JOptionPane.showMessageDialog(null, bookList.toString(), "Book Not Found", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Verificar que hay copias disponibles
            if (book.getAvailableCopies() <= 0) {
                JOptionPane.showMessageDialog(null, "No copies available for the book: " + bookTitle, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Calcular fecha de devolución (7 días después)
            LocalDate loanDate = LocalDate.now();
            LocalDate expectedReturnDate = loanDate.plusDays(7);
            
            // Crear el préstamo
            Loan loan = new Loan(member.getIdMember(), book.getIdBook(), loanDate, null, 0.0, "BORROWED", LocalDate.now().toString(), null);
            // Asignar las fechas de creación y actualización
            loan.setCreatedAt(LocalDate.now().toString());
            loan.setUpdatedAt(LocalDate.now().toString());
            
            loanDAO.addLoan(loan);

            // Reducir las copias disponibles del libro
            book.setAvailableCopies(book.getAvailableCopies() - 1);
            bookDAO.update(book);

            JOptionPane.showMessageDialog(null, 
                "Loan registered successfully!\n\n" +
                "Member: " + member.getName() + "\n" +
                "Book: " + book.getTitle() + "\n" +
                "Loan Date: " + loanDate + "\n" +
                "Expected Return Date: " + expectedReturnDate + "\n" +
                "Available copies remaining: " + book.getAvailableCopies(), 
                "Success", JOptionPane.INFORMATION_MESSAGE);
                
        } catch (DatabaseException e) {
            System.err.println("DATABASE ERROR IN REGISTRATION");
            System.err.println("Message: " + e.getMessage());
            System.err.println("Cause: " + e.getCause());
            e.printStackTrace();
            
            JOptionPane.showMessageDialog(null, 
                "Error registering loan: " + e.getMessage() + "\n\n" +
                "Check console for more details.", 
                "Database Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            System.err.println("UNEXPECTED ERROR IN REGISTRATION");
            System.err.println("Message: " + e.getMessage());
            System.err.println("Type: " + e.getClass().getSimpleName());
            e.printStackTrace();
            
            JOptionPane.showMessageDialog(null, 
                "Unexpected error: " + e.getMessage() + "\n\n" +
                "Check console for more details.", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // TEST DATABASE CONNECTION
    public void testDatabaseConnection() {
        try {
            // Probar conexión con miembros
            List<Member> members = memberDAO.getAllMembers();
            JOptionPane.showMessageDialog(null, 
                "Conexión a base de datos exitosa!\n" +
                "Miembros encontrados: " + members.size(), 
                "Test de Conexión", JOptionPane.INFORMATION_MESSAGE);
        } catch (DatabaseException e) {
            JOptionPane.showMessageDialog(null, 
                "Error de conexión a la base de datos:\n" + e.getMessage() + "\n\n" +
                "Verifica que:\n" +
                "1. MySQL esté ejecutándose\n" +
                "2. La base de datos 'LibroNova' exista\n" +
                "3. Las credenciales sean correctas (root/123456)", 
                "Error de Conexión", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Error inesperado: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // SHOW AVAILABLE MEMBERS AND BOOKS
    public void showAvailableMembersAndBooks() {
        try {
            List<Member> members = memberDAO.getAllMembers();
            List<Book> books = bookDAO.findAll();
            
            StringBuilder sb = new StringBuilder("LOAN INFORMATION\n\n");
            
            sb.append("AVAILABLE MEMBERS:\n");
            for (Member m : members) {
                sb.append("- ").append(m.getName()).append("\n");
            }
            
            sb.append("\nAVAILABLE BOOKS:\n");
            for (Book b : books) {
                if (b.getAvailableCopies() > 0) {
                    sb.append("- ").append(b.getTitle()).append(" (Author: ").append(b.getAuthor()).append(", Copies available: ").append(b.getAvailableCopies()).append(")\n");
                }
            }
            
            JOptionPane.showMessageDialog(null, sb.toString(), "Members and Books Available", JOptionPane.INFORMATION_MESSAGE);
        } catch (DatabaseException e) {
            JOptionPane.showMessageDialog(null, "Error retrieving information: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // RETURN LOAN BY NAMES
    public void returnLoanByNames(String memberName, String bookTitle) {
        try {
            // Buscar miembro por nombre
            List<Member> members = memberDAO.getAllMembers();
            Member member = members.stream()
                .filter(m -> m.getName().equalsIgnoreCase(memberName))
                .findFirst()
                .orElse(null);
            
            if (member == null) {
                JOptionPane.showMessageDialog(null, "Member not found: " + memberName, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Buscar libro por título
            List<Book> books = bookDAO.findAll();
            Book book = books.stream()
                .filter(b -> b.getTitle().equalsIgnoreCase(bookTitle))
                .findFirst()
                .orElse(null);
            
            if (book == null) {
                JOptionPane.showMessageDialog(null, "Book not found: " + bookTitle, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Buscar préstamos activos para este miembro y libro
            List<Loan> allLoans = loanDAO.getAllLoans();
            List<Loan> activeLoans = allLoans.stream()
                .filter(l -> l.getIdMember() == member.getIdMember() && 
                           l.getIdBook() == book.getIdBook() && 
                           "BORROWED".equalsIgnoreCase(l.getState()))
                .toList();

            if (activeLoans.isEmpty()) {
                JOptionPane.showMessageDialog(null, 
                    "No active loan found for:\n" +
                    "Member: " + member.getName() + "\n" +
                    "Book: " + book.getTitle(), 
                    "Loan Not Found", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Si hay múltiples préstamos, mostrar opciones
            Loan loanToReturn;
            if (activeLoans.size() == 1) {
                loanToReturn = activeLoans.get(0);
            } else {
                // Mostrar opciones si hay múltiples préstamos
                StringBuilder options = new StringBuilder("Multiple active loans found:\n\n");
                for (int i = 0; i < activeLoans.size(); i++) {
                    Loan l = activeLoans.get(i);
                    options.append(String.format("%d. Loan date: %s\n", i + 1, l.getLoanDatetime()));
                }
                options.append("\nSelect the number of the loan to return:");
                
                String choice = JOptionPane.showInputDialog(null, options.toString(), "Select Loan", JOptionPane.QUESTION_MESSAGE);
                if (choice == null) return;
                
                try {
                    int index = Integer.parseInt(choice) - 1;
                    if (index >= 0 && index < activeLoans.size()) {
                        loanToReturn = activeLoans.get(index);
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid option.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // Calcular multa automáticamente
            LocalDate returnDate = LocalDate.now();
            LocalDate expectedReturnDate = loanToReturn.getLoanDatetime().plusDays(7);
            long daysLate = 0;
            double calculatedPenalty = 0.0;
            
            if (returnDate.isAfter(expectedReturnDate)) {
                daysLate = returnDate.toEpochDay() - expectedReturnDate.toEpochDay();
                calculatedPenalty = daysLate * 1500.0; // $1500 por día según config.properties
            }
            
            // Mostrar información de multa y pedir confirmación
            String penaltyMessage = "RETURN INFORMATION:\n\n" +
                "Member: " + member.getName() + "\n" +
                "Book: " + book.getTitle() + "\n" +
                "Loan Date: " + loanToReturn.getLoanDatetime() + "\n" +
                "Expected Return Date: " + expectedReturnDate + "\n" +
                "Actual Return Date: " + returnDate + "\n";
            
            if (daysLate > 0) {
                penaltyMessage += "Days Late: " + daysLate + "\n" +
                    "Calculated Fine: $" + calculatedPenalty + " ($1500 per day)\n\n";
            } else {
                penaltyMessage += "Returned on time! No fine.\n\n";
            }
            
            penaltyMessage += "Confirm return?";
            
            int confirm = JOptionPane.showConfirmDialog(null, penaltyMessage, "Confirm Return", JOptionPane.YES_NO_OPTION);
            
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }
            
            double penalty = calculatedPenalty;

            // Debugging: Show data before updating
            System.out.println("RETURN DATA");
            System.out.println("Loan ID: " + loanToReturn.getIdLoan());
            System.out.println("Member ID: " + loanToReturn.getIdMember());
            System.out.println("Book ID: " + loanToReturn.getIdBook());
            System.out.println("Loan Date: " + loanToReturn.getLoanDatetime());
            System.out.println("Return Date: " + returnDate);
            System.out.println("Penalty: " + penalty);
            System.out.println("State: REPAY");
            System.out.println("==================");

            // Process return
            loanToReturn.setRefundDatetime(returnDate);
            loanToReturn.setPenalty(penalty);
            loanToReturn.setState("REPAY");
            
            System.out.println("Attempting to update loan in database...");
            loanDAO.updateLoan(loanToReturn);
            System.out.println("Loan updated successfully.");

            // Incrementar las copias disponibles del libro
            book.setAvailableCopies(book.getAvailableCopies() + 1);
            bookDAO.update(book);

            String successMessage = "Book returned successfully!\n\n" +
                "Member: " + member.getName() + "\n" +
                "Book: " + book.getTitle() + "\n" +
                "Return Date: " + returnDate + "\n";
            
            if (daysLate > 0) {
                successMessage += "Days Late: " + daysLate + "\n" +
                    "Fine Applied: $" + penalty + "\n";
            } else {
                successMessage += "Returned on time! No fine.\n";
            }
            
            successMessage += "Available copies now: " + book.getAvailableCopies();
            
            JOptionPane.showMessageDialog(null, successMessage, "Return Successful", JOptionPane.INFORMATION_MESSAGE);
                
        } catch (DatabaseException e) {
            System.err.println("DATABASE ERROR IN RETURN");
            System.err.println("Message: " + e.getMessage());
            System.err.println("Cause: " + e.getCause());
            e.printStackTrace();
            
            JOptionPane.showMessageDialog(null, 
                "Error processing return: " + e.getMessage() + "\n\n" +
                "Check console for more details.", 
                "Database Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            System.err.println("UNEXPECTED ERROR IN RETURN");
            System.err.println("Message: " + e.getMessage());
            System.err.println("Type: " + e.getClass().getSimpleName());
            e.printStackTrace();
            
            JOptionPane.showMessageDialog(null, 
                "Unexpected error: " + e.getMessage() + "\n\n" +
                "Check console for more details.", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
