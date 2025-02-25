package org.example.Books.Servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.Books.Book;
import org.example.Books.Service.BooksDao;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.ArrayList;

public class BookServlet extends HttpServlet {

    private static final String BOOK_HTML_PATH = "./src/main/webapp/books.html";

    private final BooksDao booksDao= new BooksDao();
    private String htmlContent;

    public BookServlet() throws SQLException {
    }

    @Override
    public void init(ServletConfig config){
        try {
            File file = new File(BOOK_HTML_PATH);
            htmlContent = Files.readString(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.setStatus(HttpServletResponse.SC_OK);

        var writer = resp.getWriter();

        ArrayList<Book> books = new ArrayList<>();
        try {
            books = booksDao.getBooks();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        for (Book book : books) {
            writer.println("<p> code: " + book.getCode() + ", title: " + book.getTitle() + ", author: " + book.getAuthor() +"</p>");
        }

        writer.println(htmlContent);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String code = req.getParameter("code");
        String title = req.getParameter("title");
        String author = req.getParameter("author");

        var writer = resp.getWriter();

        int intCode;

        try {
            intCode = Integer.parseInt(code);
        } catch (IllegalArgumentException e){
            System.out.println("doPost:: invalid Code");
            resp.sendError(HttpServletResponse.SC_UNPROCESSABLE_CONTENT, "Code must be a Number!");
            return;
        }

        if (intCode < 0){
            System.out.println("doPost:: Invalid Code");
            resp.sendError(HttpServletResponse.SC_UNPROCESSABLE_CONTENT, "Code must be a Number!");
            return;
        }

        if (code.isEmpty() | code.isBlank() | title.isEmpty() | title.isBlank() | author.isEmpty() | author.isBlank()){
            System.out.println("doPost:: All inputs must filled");
            resp.sendError(HttpServletResponse.SC_UNPROCESSABLE_CONTENT, "All inputs must filled!");
            return;
        }

        Book book = new Book(intCode, title, author);

        writer.print("<h1>Books</h1>");
        writer.print("<p></p>");

        try {
            booksDao.addBook(book);
        } catch (SQLException e) {
            System.out.println("doPost:: Code is already taken");
            resp.sendError(HttpServletResponse.SC_UNPROCESSABLE_CONTENT, "This code: " + code + " is already taken");
            return;
        }
        writer.println("<p> code: " + book.getCode() + ", title: " + book.getTitle() + ", author: " + book.getAuthor() +"/p>");
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String[] pathInfo = req.getPathInfo().split("/");
        String code = pathInfo[pathInfo.length - 1];

        if (req.getPathInfo() == null){
            System.out.println("doPost:: path is empty");
            resp.sendError(HttpServletResponse.SC_UNPROCESSABLE_CONTENT, "Path is empty");
            return;
        }


        String title = req.getParameter("title");
        String author = req.getParameter("author");

        int intCode;

        try {
            intCode = Integer.parseInt(code);
        } catch (IllegalArgumentException e){
            System.out.println("doPost:: invalid Code");
            resp.sendError(HttpServletResponse.SC_UNPROCESSABLE_CONTENT, "Code must be a Number!");
            return;
        }

        if (intCode < 0){
            System.out.println("doPost:: Invalid Code");
            resp.sendError(HttpServletResponse.SC_UNPROCESSABLE_CONTENT, "Code must be a Number!");
            return;
        }

        if (code.isEmpty() | code.isBlank() | title.isEmpty() | title.isBlank() | author.isEmpty() | author.isBlank()){
            System.out.println("doPost:: All inputs must filled");
            resp.sendError(HttpServletResponse.SC_UNPROCESSABLE_CONTENT, "All inputs must filled!");
            return;
        }

        Book book = new Book(intCode, author, title);

        try {
            booksDao.updateBook(book);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (req.getPathInfo() == null){
            System.out.println("doPost:: path is empty");
            resp.sendError(HttpServletResponse.SC_UNPROCESSABLE_CONTENT, "Path is empty");
            return;
        }

        String[] pathInfo = req.getPathInfo().split("/");
        String code = pathInfo[pathInfo.length - 1];

        int intCode;

        try {
            intCode = Integer.parseInt(code);
        } catch (IllegalArgumentException e){
            System.out.println("doPost:: invalid code");
            resp.sendError(HttpServletResponse.SC_UNPROCESSABLE_CONTENT, "invalid code");
            return;
        }

        try {
            booksDao.deleteBook(intCode);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
