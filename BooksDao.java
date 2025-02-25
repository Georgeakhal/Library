package org.example.Books.Service;

import org.example.Books.Book;
import org.example.Connector;


import java.sql.*;
import java.util.ArrayList;

public class BooksDao {
    private final Connection con = Connector.getInstance().getCon();

    public BooksDao() throws SQLException {
    }

    private final String GET_BY_ID = "SELECT * FROM public.\"Books\" WHERE \"code\" = ?";
    private final String GET_ALL = "SELECT * FROM public.\"Books\"";
    private final String ADD = "INSERT INTO public.\"Books\" (\"code\", \"title\", \"author\") VALUES (?, ?, ?)";
    private final String UPDATE = "UPDATE public.\"Books\" SET \"title\" = ?, \"author\" = ? WHERE \"code\" = ?";
    private final String DELETE = "DELETE FROM public.\"Books\" WHERE \"code\" = ?";

    public ArrayList<Book> getBooks() throws SQLException {
        ArrayList<Book> books = new ArrayList<>();

        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(GET_ALL);

        while(rs.next()){
            Book book = new Book(rs.getInt(1), rs.getString(2), rs.getString(3));
            books.add(book);
        }

        return books;
    }

    public Book getBook(int code) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(GET_BY_ID);

        stmt.setInt(1, code);
        ResultSet rs = stmt.executeQuery();
        
        Book book = null;

        if( rs.next()){
            book = new Book(rs.getInt(1), rs.getString(2), rs.getString(3));
        }

        return book;
    }

    public void addBook(Book book) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(ADD);

        stmt.setInt(1, book.getCode());
        stmt.setString(2, book.getTitle());
        stmt.setString(3, book.getAuthor());

        stmt.execute();
    }

    public void updateBook(Book book) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(UPDATE);
        stmt.setString(1, book.getTitle());
        stmt.setString(2, book.getAuthor());
        stmt.setInt(3, book.getCode());

        stmt.execute();
    }

    public void deleteBook(int code) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(DELETE);
        stmt.setInt(1, code);
        stmt.execute();
    }
}
