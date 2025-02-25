package org.example.Books;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.example.Books.Service.BooksDao;
import org.example.Books.Servlet.BookServlet;
import org.example.Members.Service.MembersDao;
import org.example.Members.Servlet.MemberServlet;

import java.io.File;
import java.sql.SQLException;

public class BookServer {
    public static void main(String[] args) throws SQLException, LifecycleException {
        Tomcat tomcat = new Tomcat();
        tomcat.setBaseDir("tomcatSvr");
        tomcat.setPort(8080);

        String contextPath = "";
        String docBase = new File("./src/main/webapp").getAbsolutePath();

        Context context = tomcat.addContext(contextPath, docBase);

        BooksDao booksDao = new BooksDao();

        Tomcat.addServlet(context, "BookServlet", new BookServlet());
        context.addServletMappingDecoded("/", "BookServlet");

        tomcat.start();
        tomcat.getConnector();
        tomcat.getServer().await();
    }
}
