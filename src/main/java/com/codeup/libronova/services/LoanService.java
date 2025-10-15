/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codeup.libronova.services;

import com.codeup.libronova.DAO.ILoanDAO;
import com.codeup.libronova.DAO.impl.LoanDAOimpl;
import com.codeup.libronova.domain.Loan;
import com.codeup.libronova.exception.DatabaseException;
import java.time.LocalDate;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Coder
 */
public class LoanService {
    private final ILoanDAO loanDAO;

    public LoanService() {
        this.loanDAO = new LoanDAOimpl();
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

            if (loan.getLoanDatetime() == null) {
                loan.setLoanDatetime(LocalDate.now());
            }

            loan.setState("BORROWED");
            loanDAO.addLoan(loan);

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

            if ("REPAID".equalsIgnoreCase(loan.getState())) {
                JOptionPane.showMessageDialog(null, "This loan has already been returned.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            loan.setRefundDatetime(LocalDate.now());
            loan.setPenalty(penalty);
            loan.setState("REPAID");
            loanDAO.updateLoan(loan);

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

            StringBuilder sb = new StringBuilder("All Loans:\n\n");
            for (Loan l : loans) {
                sb.append(l.toString()).append("\n");
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
}
