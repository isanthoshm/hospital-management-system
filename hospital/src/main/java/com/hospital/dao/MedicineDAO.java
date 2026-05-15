package com.hospital.dao;

import com.hospital.model.Medicine;
import com.hospital.util.DBConnection;
import java.sql.*;
import java.util.*;

public class MedicineDAO {

    public boolean addMedicine(Medicine m) throws SQLException {
        String sql = "INSERT INTO medicines (name, category, manufacturer, unit_price, stock_qty, expiry_date) VALUES (?,?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, m.getName());
            ps.setString(2, m.getCategory());
            ps.setString(3, m.getManufacturer());
            ps.setBigDecimal(4, m.getUnitPrice());
            ps.setInt(5, m.getStockQty());
            ps.setDate(6, m.getExpiryDate());
            return ps.executeUpdate() > 0;
        }
    }

    public List<Medicine> getAllMedicines() throws SQLException {
        List<Medicine> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM medicines ORDER BY name")) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public Medicine getMedicineById(int id) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM medicines WHERE medicine_id=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    public boolean updateMedicine(Medicine m) throws SQLException {
        String sql = "UPDATE medicines SET name=?, category=?, manufacturer=?, unit_price=?, stock_qty=?, expiry_date=? WHERE medicine_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, m.getName());
            ps.setString(2, m.getCategory());
            ps.setString(3, m.getManufacturer());
            ps.setBigDecimal(4, m.getUnitPrice());
            ps.setInt(5, m.getStockQty());
            ps.setDate(6, m.getExpiryDate());
            ps.setInt(7, m.getMedicineId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteMedicine(int id) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("DELETE FROM medicines WHERE medicine_id=?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public int countLowStock(int threshold) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) FROM medicines WHERE stock_qty <= ?")) {
            ps.setInt(1, threshold);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    private Medicine mapRow(ResultSet rs) throws SQLException {
        Medicine m = new Medicine();
        m.setMedicineId(rs.getInt("medicine_id"));
        m.setName(rs.getString("name"));
        m.setCategory(rs.getString("category"));
        m.setManufacturer(rs.getString("manufacturer"));
        m.setUnitPrice(rs.getBigDecimal("unit_price"));
        m.setStockQty(rs.getInt("stock_qty"));
        m.setExpiryDate(rs.getDate("expiry_date"));
        m.setCreatedAt(rs.getTimestamp("created_at"));
        return m;
    }
}
