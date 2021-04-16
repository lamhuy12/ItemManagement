package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;
import utils.MyConnnection;
import DTO.SupplierDTO;
import java.util.List;

public class SupplierDAO extends Vector<SupplierDTO> {

    public int find(String SupCode) {
        try {
            List<SupplierDTO> list = getAllSuppliers();
            for (int i = 0; i < list.size(); i++) {
                if (SupCode.equals(list.get(i).getSupCode())) {
                    return i;
                }
            }
        } catch (Exception e) {
        }
        return -1;
    }

    public SupplierDTO findSupplier(String SupCode) {
        try {
            List<SupplierDTO> list = getAllSuppliers();
            int i = find(SupCode);
            return i < 0 ? null : list.get(i);
        } catch (Exception e) {
        }
        return null;
    }

    public static SupplierDAO getAllSuppliers() throws Exception {
        SupplierDAO sup = new SupplierDAO();
        String sql = "Select supCode, supName, address, collaborating from tblSuppliers";
        try (Connection c = MyConnnection.makeConnection();
                PreparedStatement ps = c.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String code = rs.getString("SupCode");
                String name = rs.getString("SupName");
                String address = rs.getString("Address");
                boolean colloborating = rs.getBoolean(4);
                SupplierDTO supplier = new SupplierDTO(code, name, address, colloborating);
                sup.add(supplier);
            }
        }
        return sup;
    }
    
        public static SupplierDTO getSupplierByCode(String supCode) throws Exception {
        String sql = "Select supCode from tblSuppliers Where supCode=? COLLATE SQL_Latin1_General_CP1_CS_AS ";
        try (Connection c = MyConnnection.makeConnection();
                PreparedStatement sm = c.prepareStatement(sql)) {
            sm.setString(1, supCode);
            ResultSet rs = sm.executeQuery();
            if (rs.next()) {
                SupplierDTO e = new SupplierDTO();
                return e;
            }
        }
        return null;
    }

    public static int insertSup(SupplierDTO sup) throws Exception {
        String sql = "Insert into tblSuppliers values(?,?,?,?)";
        try (Connection c = MyConnnection.makeConnection();
                PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, sup.getSupCode());
            ps.setString(2, sup.getSupName());
            ps.setString(3, sup.getAddress());
            ps.setBoolean(4, sup.isColloborating());
            return ps.executeUpdate();
        }
    }

    public static int updateSup(SupplierDTO sup) throws Exception {
        String sql = "Update tblSuppliers set supName =?, address=?, collaborating=? where supCode=?";
        try (Connection c = MyConnnection.makeConnection();
                PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, sup.getSupName());
            ps.setString(2, sup.getAddress());
            ps.setBoolean(3, sup.isColloborating());
            ps.setString(4, sup.getSupCode());
            return ps.executeUpdate();
        }
    }

    public static int deleteSup(String supCode) throws Exception {
        String sql = "Delete From tblSuppliers Where supCode=?";
        try (Connection c = MyConnnection.makeConnection();
                PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, supCode);
            return ps.executeUpdate();
        }
    }


}
