/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.naming.NamingException;
import utils.MyConnnection;

/**
 *
 * @author HUY VU
 */
public class UserDAO implements Serializable {

    public static String checkLogin(String userID, String password)
            throws SQLException, NamingException {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = MyConnnection.makeConnection();
            if (con != null) {
                String sql = "Select fullName from tblUsers Where userID=? COLLATE SQL_Latin1_General_CP1_CS_AS "
                        + "and password =? COLLATE SQL_Latin1_General_CP1_CS_AS";

                stm = con.prepareStatement(sql);
                stm.setString(1, userID);
                stm.setString(2, password);

                rs = stm.executeQuery();
                if (rs.next()) {
                    //           return true;
                    String name = rs.getString("fullName");
                    return name;
                }
            }
        } finally {
            if (rs != null) {
                rs.close();
            }

            if (stm != null) {
                stm.close();
            }

            if (con != null) {
                con.close();
            }
        }
        //  return false;
        return null;
    }

}
