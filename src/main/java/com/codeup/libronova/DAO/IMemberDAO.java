/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codeup.libronova.DAO;

import com.codeup.libronova.domain.Member;
import com.codeup.libronova.exception.DatabaseException;
import java.util.List;

/**
 *
 * @author Coder
 */
public interface IMemberDAO {
    void addMember(Member member) throws DatabaseException;
    void updateMember(Member member) throws DatabaseException;
    void deleteMember(int idMember) throws DatabaseException;
    void deleteMemberByName(String name) throws DatabaseException;
    Member getMemberById(int idMember) throws DatabaseException;
    List<Member> getAllMembers() throws DatabaseException;
    List<Member> findMembersByName(String name) throws DatabaseException;
}



