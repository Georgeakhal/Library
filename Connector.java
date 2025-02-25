package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connector {
    private final String DB_URL = "jdbc:postgresql://localhost:5432/Library";
    private final String DB_USER = "postgres";
    private final String DB_PASS = "brb123/xd/brb431";
    private Connection con = null;

    public static Connector connector = null;


    private Connector() throws SQLException {
        this.con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }

    public static Connector getInstance() throws SQLException {
        if (connector == null){
            connector = new Connector();
            return new Connector();
        } else {
            return connector;
        }
    }

    public Connection getCon() {
        return con;
    }
}
