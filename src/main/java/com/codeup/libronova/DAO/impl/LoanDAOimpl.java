/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codeup.libronova.DAO.impl;

import com.codeup.libronova.Connection.ConexionDB;
import com.codeup.libronova.DAO.ILoanDAO;
import com.codeup.libronova.domain.Loan;
import com.codeup.libronova.exception.DatabaseException;
import java.sql.Connection;
import java.sql.Date;
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
public class LoanDAOimpl implements ILoanDAO {

    @Override
    public void addLoan(Loan loan) throws DatabaseException {
        String sql = "INSERT INTO Loans (id_member, id_book, loan_datetime, refund_datetime, penalty, state) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = new ConexionDB(null).open();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, loan.getIdMember());
            ps.setInt(2, loan.getIdBook());
            ps.setDate(3, Date.valueOf(loan.getLoanDatetime()));
            ps.setDate(4, Date.valueOf(loan.getRefundDatetime()));
            ps.setDouble(5, loan.getPenalty());
            ps.setString(6, loan.getState());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error adding new loan", e);
        }
    }

    @Override
    public void updateLoan(Loan loan) throws DatabaseException {
        String sql = "UPDATE Loans SET id_member=?, id_book=?, loan_datetime=?, refund_datetime=?, penalty=?, state=?, updated_at=NOW() "
                   + "WHERE id_loan=?";
        try (Connection conn = new ConexionDB(null).open();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, loan.getIdMember());
            ps.setInt(2, loan.getIdBook());
            ps.setDate(3, Date.valueOf(loan.getLoanDatetime()));
            ps.setDate(4, Date.valueOf(loan.getRefundDatetime()));
            ps.setDouble(5, loan.getPenalty());
            ps.setString(6, loan.getState());
            ps.setInt(7, loan.getIdLoan());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error updating loan", e);
        }
    }

    @Override
    public void deleteLoan(int idLoan) throws DatabaseException {
        String sql = "DELETE FROM Loans WHERE id_loan = ?";
        try (Connection conn = new ConexionDB(null).open();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idLoan);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error deleting loan", e);
        }
    }

    @Override
    public Loan findLoanById(int idLoan) throws DatabaseException {
        String sql = "SELECT * FROM Loans WHERE id_loan = ?";
        try (Connection conn = new ConexionDB(null).open();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idLoan);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapLoan(rs);
        } catch (SQLException e) {
            throw new DatabaseException("Error finding loan by ID", e);
        }
        return null;
    }

    @Override
    public List<Loan> getAllLoans() throws DatabaseException {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT * FROM Loans";
        try (Connection conn = new ConexionDB(null).open();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) loans.add(mapLoan(rs));
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching all loans", e);
        }
        return loans;
    }

    @Override
    public List<Loan> findLoansByMember(int idMember) throws DatabaseException {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT * FROM Loans WHERE id_member = ?";
        try (Connection conn = new ConexionDB(null).open();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idMember);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) loans.add(mapLoan(rs));
        } catch (SQLException e) {
            throw new DatabaseException("Error finding loans by member", e);
        }
        return loans;
    }

    @Override
    public List<Loan> findLoansByBook(int idBook) throws DatabaseException {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT * FROM Loans WHERE id_book = ?";
        try (Connection conn = new ConexionDB(null).open();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idBook);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) loans.add(mapLoan(rs));
        } catch (SQLException e) {
            throw new DatabaseException("Error finding loans by book", e);
        }
        return loans;
    }

    @Override
    public List<Loan> findLoansByState(String state) throws DatabaseException {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT * FROM Loans WHERE state = ?";
        try (Connection conn = new ConexionDB(null).open();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, state);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) loans.add(mapLoan(rs));
        } catch (SQLException e) {
            throw new DatabaseException("Error finding loans by state", e);
        }
        return loans;
    }

    // ===================== HELPER ==========================
    private Loan mapLoan(ResultSet rs) throws SQLException {
        Loan loan = new Loan(
            rs.getInt("id_loan"),
            rs.getInt("id_member"),
            rs.getInt("id_book"),
            rs.getDate("loan_datetime").toLocalDate(),
            rs.getDate("refund_datetime").toLocalDate(),
            rs.getDouble("penalty"),
            rs.getString("state"),
            rs.getString("created_at"),
            rs.getString("updated_at")
        );
        return loan;
    }
}


