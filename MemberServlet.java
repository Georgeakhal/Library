package org.example.Members.Servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.Books.Book;
import org.example.Books.Service.BooksDao;
import org.example.Members.Member;
import org.example.Members.Service.MembersDao;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class MemberServlet extends HttpServlet {
    private static final String MEMBER_HTML_PATH = "./src/main/webapp/members.html";

    private final MembersDao membersDao = new MembersDao();
    private String htmlContent;

    public MemberServlet() throws SQLException {
    }



    @Override
    public void init(ServletConfig config){
        try {
            File file = new File(MEMBER_HTML_PATH);
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

        ArrayList<Member> members = new ArrayList<>();
        try {
            members = membersDao.getMembers();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        for (Member member : members) {
            writer.println("<p> id: " + member.getId() + ", name: " + member.getName() + ", email: " + member.getEmail() + ", join date: " + member.getJoin_date() +"</p>");
        }

        writer.println(htmlContent);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String id = UUID.randomUUID().toString();
        var writer = resp.getWriter();

        if (name.isEmpty() | name.isBlank() | email.isEmpty() | email.isBlank()){
            System.out.println("doPost:: All inputs must filled");
            resp.sendError(HttpServletResponse.SC_UNPROCESSABLE_CONTENT, "All inputs must filled!");
            return;
        }

        Member member = new Member(id, name, email, LocalDate.now());

        writer.print("<h1>Members</h1>");
        writer.print("<p></p>");

        try {
            membersDao.addMember(member);
        } catch (SQLException e) {
            System.out.println("doPost:: This email is already taken");
            resp.sendError(HttpServletResponse.SC_UNPROCESSABLE_CONTENT, "All inputs must filled!");
            return;
        }
        writer.print("<p> id: " + member.getId() + ", name: " + member.getName() + ", email: " + member.getEmail() + ", join date: " + member.getJoin_date() +"/p>");

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String[] pathInfo = req.getPathInfo().split("/");
        String id = pathInfo[pathInfo.length - 1];

        String name = req.getParameter("name");
        String email = req.getParameter("email");

        if (name.isEmpty() | name.isBlank() | email.isEmpty() | email.isBlank()){
            System.out.println("doPost:: All inputs must filled");
            resp.sendError(HttpServletResponse.SC_UNPROCESSABLE_CONTENT, "All inputs must filled!");
            return;
        }

        Member member = new Member(id, name, email);

        try {
            membersDao.updateMember(member);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getPathInfo() == null){
            System.out.println("doPost:: Path is empty");
            resp.sendError(HttpServletResponse.SC_UNPROCESSABLE_CONTENT, "Path is empty!");
            return;
        }

        String[] pathInfo = req.getPathInfo().split("/");
        String id = pathInfo[pathInfo.length - 1];

        try {
            membersDao.deleteMember(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
