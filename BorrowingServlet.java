package org.example.Borrowings.Servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.Borrowings.Borrowing;
import org.example.Borrowings.Service.BorrowingsDao;
import org.example.Members.Member;
import org.example.Members.Service.MembersDao;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class BorrowingServlet extends HttpServlet {
    private static final String BORROWING_HTML_PATH = "./src/main/webapp/borrowings.html";

    private final BorrowingsDao borrowingsDao = new BorrowingsDao();
    private String htmlContent;

    public BorrowingServlet() throws SQLException {
    }


    @Override
    public void init(ServletConfig config){
        try {
            File file = new File(BORROWING_HTML_PATH);
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

        ArrayList<Borrowing> borrowings = new ArrayList<>();
        try {
            borrowings = borrowingsDao.getBorrowings();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        writer.print("<h1>Borrowing</h1>");
        writer.print("<p></p>");

        for (Borrowing borrowing : borrowings) {
            writer.println("<p> Book code: " + borrowing.getCode() + ", Member id: " + borrowing.getId() + ", Borrow date: " + borrowing.getBorrowDate() + ", Return date: " + borrowing.getReturnDate() + "</p>");
        }

        writer.println(htmlContent);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String code = req.getParameter("bookCode");
        String id = req.getParameter("memberId");

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
            resp.sendError(HttpServletResponse.SC_UNPROCESSABLE_CONTENT, "Invalid Code");
            return;
        }


        if (id == null){
            LocalDate date = LocalDate.now();

            if (code.isEmpty() | code.isBlank()){
                System.out.println("doPost:: All inputs must filled");
                resp.sendError(HttpServletResponse.SC_UNPROCESSABLE_CONTENT, "All inputs must filled!");
                return;
            }

            Borrowing borrowing;

            try {
                borrowing = borrowingsDao.getLastBorrowingByBookCode(intCode);
            } catch (SQLException e) {
                borrowing = null;
            }

            if (borrowing == null){
                System.out.println("doPost:: You cant return Book that is not borrowed");
                resp.sendError(HttpServletResponse.SC_UNPROCESSABLE_CONTENT, "You can return Book that is not borrowed");
                return;
            }

            try {
                borrowingsDao.returnBook(intCode, LocalDate.now());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            return;
        }

        LocalDate date = LocalDate.now();

        if (code.isEmpty() | code.isBlank() | id.isEmpty() | id.isBlank()){
            System.out.println("doPost:: All inputs must filled");
            resp.sendError(HttpServletResponse.SC_UNPROCESSABLE_CONTENT, "All inputs must filled!");
            return;
        }

        Borrowing existingBorrowing = null;

        try {
            existingBorrowing = borrowingsDao.getLastBorrowingByBookCode(intCode);
        } catch (SQLException e) {
            System.out.println("Something went wrong");
        }

        if (existingBorrowing != null){
            if (existingBorrowing.getReturnDate() == null){
                System.out.println("doPost:: This book is already borrowed");
                resp.sendError(HttpServletResponse.SC_UNPROCESSABLE_CONTENT, "This book is already borrowed");
                return;
            }
        }

        Borrowing borrowing = new Borrowing(intCode, id, date);

        try {
            borrowingsDao.addBorrowing(borrowing);
        } catch (SQLException e) {
            System.out.println("doPost:: Either (Book or Member doesn't exists) or something else went wrong");
            resp.sendError(HttpServletResponse.SC_UNPROCESSABLE_CONTENT, "doPost:: Either (Book or Member doesn't exists) or something else went wrong");
            return;
        }
    }
}
