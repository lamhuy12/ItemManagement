/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyConnnection implements Serializable {

    public static Connection makeConnection() throws SQLException {
        try {
            String userName = "sa";
            String password = "lamhuy10a1";
            String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
            Class.forName(driver);
            String url = "jdbc:sqlserver://localhost:1433; databaseName=ItemManagement; instanceName=SE140164\\SQLEXPRESS";
            Connection con = DriverManager.getConnection(url, userName, password);
            return con;
        } catch (ClassNotFoundException ex) {
//            ex.printStackTrace();
        } catch (SQLException ex) {
//            ex.printStackTrace();
        }
        return null;
    }
}
