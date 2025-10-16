/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codeup.libronova.domain;

/**
 *
 * @author Coder
 */
public class Book {
    private int idBook;
    private String isbn;
    private String title;
    private String author;
    private String category;
    private int copies;
    private int availableCopies;
    private double priceReference;
    private boolean isActivo;
    private String createdAt;
    private String updatedAt;

   
    //CONTRUCTOR SIN created
    public Book(String isbn, String title, String author, String category, int copies, int availableCopies, double priceReference, boolean isActivo) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.category = category;
        this.copies = copies;
        this.availableCopies = availableCopies;
        this.priceReference = priceReference;
        this.isActivo = isActivo;
    }

    public Book(String isbn, String title, String author, String category, int copies, int availableCopies, double priceReference, boolean isActivo, String createdAt) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.category = category;
        this.copies = copies;
        this.availableCopies = availableCopies;
        this.priceReference = priceReference;
        this.isActivo = isActivo;
        this.createdAt = createdAt;
    }
    
    // Getters and Setters

    public int getIdBook() {
        return idBook;
    }

    public void setIdBook(int idBook) {
        this.idBook = idBook;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getCopies() {
        return copies;
    }

    public void setCopies(int copies) {
        this.copies = copies;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }

    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }

    public double getPriceReference() {
        return priceReference;
    }

    public void setPriceReference(double priceReference) {
        this.priceReference = priceReference;
    }

    public boolean isIsActivo() {
        return isActivo;
    }

    public void setIsActivo(boolean isActivo) {
        this.isActivo = isActivo;
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
        return "Book{" 
                + "idBook=" + idBook 
                + ", isbn=" + isbn 
                + ", title=" + title 
                + ", author=" + author 
                + ", category=" + category 
                + ", copies=" + copies 
                + ", availableCopies=" + availableCopies 
                + ", priceReference=" + priceReference 
                + ", isActivo=" + isActivo 
                + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + '}';
    }  
    
}

