package com.example.miniproject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Db {
    public static Connection getCon() throws android.database.SQLException {
        try {
            return DriverManager.getConnection
                    ("jdbc:mysql://10.0.2.2:3306/teachingsystem", "root", "");
        } catch (SQLException e) {
            throw new android.database.SQLException("error");
        }
    }
}
