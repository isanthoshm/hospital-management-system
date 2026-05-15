package com.hospital.dao;

import com.hospital.model.Bill;
import com.hospital.util.DBConnection;
import java.sql.*;
import java.util.*;

public class BillDAO {

    public boolean addBill(Bill b) throws SQLException {
        String sql = "INSERT INTO bills (patient_id, bill_date, consultation_fee, room_charges, medicine_charges, lab_charges, other_charges, discount, total_amount, paid_amount, payment_method, status) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, b.getPatientId());
            ps.setDate(2, b.getBillDate());
            ps.setBigDecimal(3, b.getConsultationFee());
            ps.setBigDecimal(4, b.getRoomCharges());
            ps.setBigDecimal(5, b.getMedicineCharges());
            ps.setBigDecimal(6, b.getLabCharges());
            ps.setBigDecimal(7, b.getOtherCharges());
            ps.setBigDecimal(8, b.getDiscount());
            ps.setBigDecimal(9, b.getTotalAmount());
            ps.setBigDecimal(10, b.getPaidAmount());
            ps.setString(11, b.getPaymentMethod());
            ps.setString(12, b.getStatus());
            return ps.executeUpdate() > 0;
        }
    }

    public List<Bill> getAllBills() throws SQLException {
        List<Bill> list = new ArrayList<>();
        String sql = "SELECT b.*, p.full_name AS patient_name FROM bills b JOIN patients p ON b.patient_id=p.patient_id ORDER BY b.bill_date DESC";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public Bill getBillById(int id) throws SQLException {
        String sql = "SELECT b.*, p.full_name AS patient_name FROM bills b JOIN patients p ON b.patient_id=p.patient_id WHERE b.bill_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    public boolean updateBill(Bill b) throws SQLException {
        String sql = "UPDATE bills SET consultation_fee=?, room_charges=?, medicine_charges=?, lab_charges=?, other_charges=?, discount=?, total_amount=?, paid_amount=?, payment_method=?, status=? WHERE bill_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setBigDecimal(1, b.getConsultationFee());
            ps.setBigDecimal(2, b.getRoomCharges());
            ps.setBigDecimal(3, b.getMedicineCharges());
            ps.setBigDecimal(4, b.getLabCharges());
            ps.setBigDecimal(5, b.getOtherCharges());
            ps.setBigDecimal(6, b.getDiscount());
            ps.setBigDecimal(7, b.getTotalAmount());
            ps.setBigDecimal(8, b.getPaidAmount());
            ps.setString(9, b.getPaymentMethod());
            ps.setString(10, b.getStatus());
            ps.setInt(11, b.getBillId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteBill(int id) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("DELETE FROM bills WHERE bill_id=?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private Bill mapRow(ResultSet rs) throws SQLException {
        Bill b = new Bill();
        b.setBillId(rs.getInt("bill_id"));
        b.setPatientId(rs.getInt("patient_id"));
        b.setBillDate(rs.getDate("bill_date"));
        b.setConsultationFee(rs.getBigDecimal("consultation_fee"));
        b.setRoomCharges(rs.getBigDecimal("room_charges"));
        b.setMedicineCharges(rs.getBigDecimal("medicine_charges"));
        b.setLabCharges(rs.getBigDecimal("lab_charges"));
        b.setOtherCharges(rs.getBigDecimal("other_charges"));
        b.setDiscount(rs.getBigDecimal("discount"));
        b.setTotalAmount(rs.getBigDecimal("total_amount"));
        b.setPaidAmount(rs.getBigDecimal("paid_amount"));
        b.setPaymentMethod(rs.getString("payment_method"));
        b.setStatus(rs.getString("status"));
        b.setCreatedAt(rs.getTimestamp("created_at"));
        try { b.setPatientName(rs.getString("patient_name")); } catch (Exception ignored) {}
        return b;
    }
}
