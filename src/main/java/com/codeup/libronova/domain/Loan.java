/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codeup.libronova.domain;

import java.time.LocalDate;

/**
 *
 * @author Coder
 */
public class Loan {
    private int idLoan;
    private int idMember;
    private int idBook;
    private LocalDate loanDatetime;
    private LocalDate refundDatetime;
    private double penalty;
    private String state; // BORROWED or REPAY
    private String createdAt;
    private String updatedAt;

    public Loan(int idMember, int idBook,
                LocalDate loanDatetime, LocalDate refundDatetime,
                double penalty, String state,
                String createdAt, String updatedAt) {
        this.idMember = idMember;
        this.idBook = idBook;
        this.loanDatetime = loanDatetime;
        this.refundDatetime = refundDatetime;
        this.penalty = penalty;
        this.state = state;
    }
    

    public Loan(int idLoan, int idMember, int idBook, LocalDate loanDatetime, LocalDate refundDatetime, double penalty, String state, String createdAt, String updatedAt) {
        this.idLoan = idLoan;
        this.idMember = idMember;
        this.idBook = idBook;
        this.loanDatetime = loanDatetime;
        this.refundDatetime = refundDatetime;
        this.penalty = penalty;
        this.state = state;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    

    // Getters and Setters

    public int getIdLoan() {
        return idLoan;
    }

    public void setIdLoan(int idLoan) {
        this.idLoan = idLoan;
    }

    public int getIdMember() {
        return idMember;
    }

    public void setIdMember(int idMember) {
        this.idMember = idMember;
    }

    public int getIdBook() {
        return idBook;
    }

    public void setIdBook(int idBook) {
        this.idBook = idBook;
    }

    public LocalDate getLoanDatetime() {
        return loanDatetime;
    }

    public void setLoanDatetime(LocalDate loanDatetime) {
        this.loanDatetime = loanDatetime;
    }

    public LocalDate getRefundDatetime() {
        return refundDatetime;
    }

    public void setRefundDatetime(LocalDate refundDatetime) {
        this.refundDatetime = refundDatetime;
    }

    public double getPenalty() {
        return penalty;
    }

    public void setPenalty(double penalty) {
        this.penalty = penalty;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Loan{" + "idLoan=" + idLoan + ", idMember=" + idMember + ", idBook=" + idBook + ", loanDatetime=" + loanDatetime + ", refundDatetime=" + refundDatetime + ", penalty=" + penalty + ", state=" + state + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + '}';
    }
    
    
}

