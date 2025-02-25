package org.example.Borrowings.Service;

import org.example.Books.Book;
import org.example.Borrowings.Borrowing;
import org.example.Connector;

import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;

public class BorrowingsDao {
    private final Connection con = Connector.getInstance().getCon();

    public BorrowingsDao() throws SQLException {
    }

    private final String GET_BY_CODE = "SELECT * FROM public.\"Borrowings\" WHERE \"bookCode\" = ?";
    private final String GET_ALL = "SELECT * FROM public.\"Borrowings\"";
    private final String ADD = "INSERT INTO public.\"Borrowings\" (\"bookCode\", \"memberId\", \"borrowDate\") VALUES (?, ?, ?)";
    private final String UPDATE = "UPDATE public.\"Borrowings\" SET \"returnDate\" = ? WHERE \"bookCode\" = ? AND \"returnDate\" IS NULL";
    private final String DELETE = "DELETE FROM public.\"Borrowings\" WHERE \"code\" = ?";

    public ArrayList<Borrowing> getBorrowings() throws SQLException {
        ArrayList<Borrowing> borrowings = new ArrayList<>();

        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(GET_ALL);

        while(rs.next()){
            if (rs.getDate(4) == null){
                Borrowing borrowing = new Borrowing(rs.getInt(1), rs.getString(2), rs.getDate(3).toLocalDate(), null);
                borrowings.add(borrowing);
            } else {
                Borrowing borrowing = new Borrowing(rs.getInt(1), rs.getString(2), rs.getDate(3).toLocalDate(), rs.getDate(4).toLocalDate());
                borrowings.add(borrowing);
            }
        }

        return borrowings;
    }

    public Borrowing getLastBorrowingByBookCode(int code) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(GET_BY_CODE);
        stmt.setInt(1, code);

        ResultSet rs = stmt.executeQuery();

        Borrowing borrowing = null;

        while (rs.next()){
            if (rs.getDate(4) == null){
                borrowing = new Borrowing(rs.getInt(1), rs.getString(2), rs.getDate(3).toLocalDate(), null);
            } else {
                borrowing = new Borrowing(rs.getInt(1), rs.getString(2), rs.getDate(3).toLocalDate(), rs.getDate(4).toLocalDate());
            }
        }

        return borrowing;
    }

    public void addBorrowing(Borrowing borrowing) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(ADD);
        stmt.setInt(1, borrowing.getCode());
        stmt.setString(2, borrowing.getId());
        stmt.setDate(3, Date.valueOf(borrowing.getBorrowDate()));

        stmt.execute();
    }

    public void returnBook(int code, LocalDate date) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(UPDATE);

        stmt.setDate(1, Date.valueOf(date));

        stmt.setInt(2, code);

        stmt.execute();
        System.out.println();
    }
}
