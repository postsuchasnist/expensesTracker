package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * class that creates a database connection.
 * the private fields are the url of local mysql db, user name, and password
 */

public class DataBaseConnection {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/dbexpenses";
    private static final String USER = "root";
    private static final String PASSWORD = "Madlee28";

    public static Connection getConnection() {
        Connection conn = null;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        if(conn != null){
            System.out.println("Connection established");
        }
        return conn;
    }
}
