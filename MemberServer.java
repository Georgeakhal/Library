package org.example.Members;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.example.Members.Service.MembersDao;
import org.example.Members.Servlet.MemberServlet;

import java.io.File;
import java.sql.SQLException;

public class MemberServer {
    public static void main(String[] args) throws SQLException, LifecycleException {
        Tomcat tomcat = new Tomcat();
        tomcat.setBaseDir("tomcatSvr");
        tomcat.setPort(8080);

        String contextPath = "";
        String docBase = new File("./src/main/webapp").getAbsolutePath();

        Context context = tomcat.addContext(contextPath, docBase);

        MembersDao membersDao = new MembersDao();

        Tomcat.addServlet(context, "MemberServlet", new MemberServlet());
        context.addServletMappingDecoded("/", "MemberServlet");

        tomcat.start();
        tomcat.getConnector();
        tomcat.getServer().await();
    }
}

