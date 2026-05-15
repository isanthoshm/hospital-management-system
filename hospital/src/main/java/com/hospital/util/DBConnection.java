package com.hospital.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String DB_URL  = "jdbc:mysql://localhost:3306/hospital_db"
                                        + "?useSSL=false"
                                        + "&serverTimezone=UTC"
                                        + "&allowPublicKeyRetrieval=true"
                                        + "&useUnicode=true"
                                        + "&characterEncoding=UTF-8"
                                        + "&autoReconnect=true";
    private static final String USER     = "root";
    private static final String PASSWORD = "admin@123";  // your password

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✅ MySQL Driver loaded.");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ MySQL Driver NOT found: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASSWORD);
    }
}
