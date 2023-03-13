package com.wtricks.ecommerse;
import java.sql.*;

public class DBConnection {
    private static Connection conn;

     public static Connection getConnection() {
         // if already connected, simply return previous connection.
         if (conn != null) return conn;

         try {
             // create new connection
             Class.forName("com.mysql.cj.jdbc.Driver");
             conn = DriverManager.getConnection(Config.URL, Config.USERNAME, Config.PASSWORD);
         } catch(Exception e) {
             // handle error here
             System.out.println(e);
         }

         return conn;
     }
}
