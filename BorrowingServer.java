package org.example.Borrowings;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.example.Borrowings.Service.BorrowingsDao;
import org.example.Borrowings.Servlet.BorrowingServlet;

import java.io.File;
import java.sql.SQLException;

public class BorrowingServer {
    public static void main(String[] args) throws SQLException, LifecycleException {
        Tomcat tomcat = new Tomcat();
        tomcat.setBaseDir("tomcatSvr");
        tomcat.setPort(8080);

        String contextPath = "";
        String docBase = new File("./src/main/webapp").getAbsolutePath();

        Context context = tomcat.addContext(contextPath, docBase);

        BorrowingsDao borrowingsDao = new BorrowingsDao();

        Tomcat.addServlet(context, "BorrowingServlet", new BorrowingServlet());
        context.addServletMappingDecoded("/", "BorrowingServlet");

        tomcat.start();
        tomcat.getConnector();
        tomcat.getServer().await();
    }
}
