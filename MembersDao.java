package org.example.Members.Service;

import org.example.Books.Book;
import org.example.Connector;
import org.example.Members.Member;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class MembersDao {
    private final Connection con = Connector.getInstance().getCon();

    public MembersDao() throws SQLException {
    }

    private final String GET_BY_ID = "SELECT * FROM public.\"Members\" WHERE \"id\" = ?";
    private final String GET_ALL = "SELECT * FROM public.\"Members\"";
    private final String ADD = "INSERT INTO public.\"Members\" (\"id\", \"name\", \"email\", \"join_date\") VALUES (?, ?, ?, ?)";
    private final String UPDATE = "UPDATE public.\"Members\" SET \"name\" = ?, \"email\" = ?, \"join_date\" = ? WHERE \"id\" = ?";
    private final String DELETE = "DELETE FROM public.\"Members\" WHERE \"id\" = ?";

    public ArrayList<Member> getMembers() throws SQLException {
        ArrayList<Member> members = new ArrayList<>();

        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(GET_ALL);

        while(rs.next()){
            Member member = new Member(rs.getString(1), rs.getString(2), rs.getString(3), rs.getDate(4).toLocalDate());
            members.add(member);
        }

        return members;
    }

    public Member getMember(String id) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(GET_BY_ID);

        stmt.setString(1, id);
        ResultSet rs = stmt.executeQuery();

        Member member = null;

        if (rs.next()){
            member = new Member(rs.getString(1), rs.getString(2), rs.getString(3), rs.getDate(4).toLocalDate());
        }

        return member;
    }

    public void addMember(Member member) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(ADD);

        stmt.setString(1, member.getId());
        stmt.setString(2, member.getName());
        stmt.setString(3, member.getEmail());
        stmt.setDate(4, Date.valueOf(LocalDate.now()));

        stmt.execute();
    }

    public void updateMember(Member member) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(UPDATE);
        stmt.setString(1, member.getName());
        stmt.setString(2, member.getEmail());
        stmt.setDate(3, Date.valueOf(LocalDate.now()));

        stmt.setString(4, member.getId());

        stmt.execute();
    }

    public void deleteMember(String id) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(DELETE);
        stmt.setString(1, id);
        stmt.executeUpdate();
    }
}
