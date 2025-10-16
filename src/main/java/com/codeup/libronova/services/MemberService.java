/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codeup.libronova.services;

import com.codeup.libronova.DAO.IMemberDAO;
import com.codeup.libronova.DAO.impl.MemberDAOimpl;
import com.codeup.libronova.domain.Member;
import com.codeup.libronova.exception.DatabaseException;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Coder
 */
public class MemberService {

    private IMemberDAO memberDAO;

    public MemberService() {
        this.memberDAO = new MemberDAOimpl();
    }

    // CREATE 
    public void addMember(Member member) {
        try {
            if (member.getName() == null || member.getName().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Member name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            memberDAO.addMember(member);
            JOptionPane.showMessageDialog(null, "Member added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (DatabaseException e) {
            JOptionPane.showMessageDialog(null, "Error adding member: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // UPDATE 
    public void updateMember(Member member) {
        try {
            memberDAO.updateMember(member);
            JOptionPane.showMessageDialog(null, "Member updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (DatabaseException e) {
            JOptionPane.showMessageDialog(null, "Error updating member: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // DELETE 
    public void deleteMemberByName(String name) {
        try {
            List<Member> members = memberDAO.getAllMembers();
            Member found = members.stream()
                    .filter(m -> m.getName().equalsIgnoreCase(name))
                    .findFirst()
                    .orElse(null);

            if (found == null) {
                JOptionPane.showMessageDialog(null, "No member found with name: " + name, "Not Found", JOptionPane.WARNING_MESSAGE);
                return;
            }

            memberDAO.deleteMember(found.getIdMember());
            JOptionPane.showMessageDialog(null, "Member '" + name + "' deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (DatabaseException e) {
            JOptionPane.showMessageDialog(null, "Error deleting member: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // GET BY ID 
    public void getMemberById(int id) {
        try {
            Member member = memberDAO.getMemberById(id);
            if (member == null) {
                JOptionPane.showMessageDialog(null, "No member found with ID: " + id, "Not Found", JOptionPane.WARNING_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(null, member.toString(), "Member Details", JOptionPane.INFORMATION_MESSAGE);
        } catch (DatabaseException e) {
            JOptionPane.showMessageDialog(null, "Error fetching member: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // GET ALL 
    public void listAllMembers() {
        try {
            List<Member> members = memberDAO.getAllMembers();
            if (members.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No members registered.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            StringBuilder sb = new StringBuilder("Members List\n\n");
            for (Member m : members) {
                sb.append(String.format("ID: %d | Name: %s | Email: %s | Phone: %s | State: %s\n",
                        m.getIdMember(), m.getName(), m.getEmail(), m.getPhone(), m.getState()));
            }

            JOptionPane.showMessageDialog(null, sb.toString(), "All Members", JOptionPane.INFORMATION_MESSAGE);

        } catch (DatabaseException e) {
            JOptionPane.showMessageDialog(null, "Error listing members: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // SEARCH BY NAME 
    public void searchMemberByName(String name) {
        try {
            List<Member> members = memberDAO.getAllMembers();
            List<Member> results = members.stream()
                    .filter(m -> m.getName().toLowerCase().contains(name.toLowerCase()))
                    .toList();

            if (results.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No members found with name containing: " + name, "Not Found", JOptionPane.WARNING_MESSAGE);
                return;
            }

            StringBuilder sb = new StringBuilder("Search Results\n\n");
            for (Member m : results) {
                sb.append(String.format("ID: %d | Name: %s | Email: %s | Phone: %s | State: %s\n",
                        m.getIdMember(), m.getName(), m.getEmail(), m.getPhone(), m.getState()));
            }

            JOptionPane.showMessageDialog(null, sb.toString(), "Member Search", JOptionPane.INFORMATION_MESSAGE);

        } catch (DatabaseException e) {
            JOptionPane.showMessageDialog(null, "Error searching member: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

