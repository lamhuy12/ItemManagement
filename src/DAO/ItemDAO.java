 package DAO;

import DTO.ItemDTO;
import DTO.SupplierDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;
import utils.MyConnnection;

public class ItemDAO extends Vector<ItemDTO> {

    public static ItemDAO getAllItems() throws Exception {
        SupplierDAO sup = new SupplierDAO();
        ItemDAO it = new ItemDAO();
        String sql = "Select itemCode, itemName, unit, price, supplying, supCode from tblItems";
        try (Connection c = MyConnnection.makeConnection();
                PreparedStatement ps = c.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String code = rs.getString("itemCode");
                String name = rs.getString("itemName");
                String unit = rs.getString("unit");
                float price = rs.getFloat("price");
                boolean supplying = rs.getBoolean("supplying");
                String supCode = rs.getString("supCode");
                SupplierDTO supplier = sup.findSupplier(supCode);
                ItemDTO items = new ItemDTO(code, name, unit, price, supplying, supplier);
                it.add(items);
            }
        }
        return it;
    }

    public static int insertItem(ItemDTO it) throws Exception {
        String sql = "Insert into tblItems Values(?,?,?,?,?,?)";
        try (Connection c = MyConnnection.makeConnection();
                PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, it.getItemCode());
            ps.setString(2, it.getItemName());
            ps.setString(3, it.getUnit());
            ps.setFloat(4, it.getPrice());
            ps.setBoolean(5, it.isSupplying());
            ps.setString(6, it.getSupplierDTO().getSupCode());
            return ps.executeUpdate();
        }
    }

    public static int update(ItemDTO it) throws Exception {
        String sql = "Update tblItems set itemName =?, unit=?, price=?, supplying=?, supCode=? where itemCode =?";
        try (Connection c = MyConnnection.makeConnection();
                PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, it.getItemName());
            ps.setString(2, it.getUnit());
            ps.setFloat(3, it.getPrice());
            ps.setBoolean(4, it.isSupplying());
            ps.setString(5, it.getSupplierDTO().getSupCode());
            ps.setString(6, it.getItemCode());
            return ps.executeUpdate();
        }
    }
    
    public static int delete(String itemCode) throws Exception {
        String sql = "Delete from tblItems where itemCode=?";
        try (Connection c = MyConnnection.makeConnection();
                PreparedStatement ps = c.prepareStatement(sql)) {
          ps.setString(1, itemCode);
          return ps.executeUpdate();
        } 
    }
    
    public static ItemDAO getItemsByCode(String itemCode) throws Exception {
        String sql = "Select itemCode From tblItems Where itemCode=? COLLATE SQL_Latin1_General_CP1_CS_AS ";
        try (Connection c = MyConnnection.makeConnection();
                PreparedStatement sm = c.prepareStatement(sql)) {
            sm.setString(1, itemCode);
            ResultSet rs = sm.executeQuery();
            if (rs.next()) {
                //SupplierDTO supplier = suppliers.findSupplier(rs.getString("supCode"));
                ItemDAO it = new ItemDAO();
                return it;
            }
        }
        return null;
    }

}
