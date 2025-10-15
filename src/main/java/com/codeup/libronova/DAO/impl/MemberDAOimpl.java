/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codeup.libronova.DAO.impl;

import com.codeup.libronova.Connection.ConexionDB;
import com.codeup.libronova.DAO.IMemberDAO;
import com.codeup.libronova.domain.Member;
import com.codeup.libronova.exception.DatabaseException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Coder
 */
public class MemberDAOimpl implements IMemberDAO {

    private final ConexionDB conexion;

    public MemberDAOimpl(ConexionDB conexion) {
        this.conexion = conexion;
    }

    public MemberDAOimpl() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void addMember(Member member) throws DatabaseException {
        String sql = "INSERT INTO Members (name, email, phone, state, created_at, updated_at) VALUES (?, ?, ?, ?, NOW(), NOW())";
        try (Connection conn = conexion.open();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, member.getName());
            ps.setString(2, member.getEmail());
            ps.setString(3, member.getPhone());
            ps.setString(4, member.getState());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Error adding member", e);
        }
    }

    @Override
    public void updateMember(Member member) throws DatabaseException {
        String sql = "UPDATE Members SET name=?, email=?, phone=?, state=?, updated_at=NOW() WHERE id_member=?";
        try (Connection conn = conexion.open();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, member.getName());
            ps.setString(2, member.getEmail());
            ps.setString(3, member.getPhone());
            ps.setString(4, member.getState());
            ps.setInt(5, member.getIdMember());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Error updating member", e);
        }
    }

    @Override
    public void deleteMember(int idMember) throws DatabaseException {
        String sql = "DELETE FROM Members WHERE id_member=?";
        try (Connection conn = conexion.open();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idMember);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Error deleting member by ID", e);
        }
    }

    @Override
    public void deleteMemberByName(String name) throws DatabaseException {
        String sql = "DELETE FROM Members WHERE name=?";
        try (Connection conn = conexion.open();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Error deleting member by name", e);
        }
    }

    @Override
    public Member getMemberById(int idMember) throws DatabaseException {
        String sql = "SELECT * FROM Members WHERE id_member=?";
        try (Connection conn = conexion.open();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idMember);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) return mapMember(rs);
            return null;

        } catch (SQLException e) {
            throw new DatabaseException("Error fetching member by ID", e);
        }
    }

    @Override
    public List<Member> getAllMembers() throws DatabaseException {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT * FROM Members";

        try (Connection conn = conexion.open();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                members.add(mapMember(rs));
            }

        } catch (SQLException e) {
            throw new DatabaseException("Error fetching all members", e);
        }

        return members;
    }

    @Override
    public List<Member> findMembersByName(String name) throws DatabaseException {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT * FROM Members WHERE name LIKE ?";
        try (Connection conn = conexion.open();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + name + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                members.add(mapMember(rs));
            }

        } catch (SQLException e) {
            throw new DatabaseException("Error searching members by name", e);
        }
        return members;
    }

    // ===================== Helper ======================
    private Member mapMember(ResultSet rs) throws SQLException {
        return new Member(
                rs.getInt("id_member"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("phone"),
                rs.getString("state"),
                rs.getString("created_at"),
                rs.getString("updated_at")
        );
    }
}





