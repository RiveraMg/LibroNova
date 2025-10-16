/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codeup.libronova.domain;

/**
 *
 * @author Coder
 */
public class Member {
    private int idMember;
    private String name;
    private String email;
    private String phone;
    private String state; // Active or Inactive
    private String createdAt;
    private String updatedAt;

    public Member(int idMember, String name, String email, String phone, String state, String createdAt, String updatedAt) {
        this.idMember = idMember;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.state = state;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Member(int idMember, String name, String email, String phone, String state, String createdAt) {
        this.idMember = idMember;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.state = state;
        this.createdAt = createdAt;
    }

    // Getters and Setters

    public int getIdMember() {
        return idMember;
    }

    public void setIdMember(int idMember) {
        this.idMember = idMember;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
        return "Member{" + "idMember=" + idMember + ", name=" + name + ", email=" + email + ", phone=" + phone + ", state=" + state + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + '}';
    }
    
    
}


