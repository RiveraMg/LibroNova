/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codeup.libronova.DAO;

import com.codeup.libronova.domain.Loan;
import com.codeup.libronova.exception.DatabaseException;
import java.util.List;

/**
 *
 * @author Coder
 */
public interface ILoanDAO {

    void addLoan(Loan loan) throws DatabaseException;

    void updateLoan(Loan loan) throws DatabaseException;

    void deleteLoan(int idLoan) throws DatabaseException;

    Loan findLoanById(int idLoan) throws DatabaseException;

    List<Loan> getAllLoans() throws DatabaseException;

    List<Loan> findLoansByMember(int idMember) throws DatabaseException;

    List<Loan> findLoansByBook(int idBook) throws DatabaseException;

    List<Loan> findLoansByState(String state) throws DatabaseException;
}


