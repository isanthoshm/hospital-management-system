package com.hospital.dao;

import com.hospital.model.Staff;
import com.hospital.util.DBConnection;
import java.sql.*;
import java.util.*;

public class StaffDAO {

    public boolean addStaff(Staff s) throws SQLException {
        String sql = "INSERT INTO staff (full_name, role, department, phone, email, salary, join_date, status) VALUES (?,?,?,?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, s.getFullName());
            ps.setString(2, s.getRole());
            ps.setString(3, s.getDepartment());
            ps.setString(4, s.getPhone());
            ps.setString(5, s.getEmail());
            ps.setBigDecimal(6, s.getSalary());
            ps.setDate(7, s.getJoinDate());
            ps.setString(8, s.getStatus() != null ? s.getStatus() : "Active");
            return ps.executeUpdate() > 0;
        }
    }

    public List<Staff> getAllStaff() throws SQLException {
        List<Staff> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM staff ORDER BY full_name")) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public Staff getStaffById(int id) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM staff WHERE staff_id=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    public boolean updateStaff(Staff s) throws SQLException {
        String sql = "UPDATE staff SET full_name=?, role=?, department=?, phone=?, email=?, salary=?, join_date=?, status=? WHERE staff_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, s.getFullName());
            ps.setString(2, s.getRole());
            ps.setString(3, s.getDepartment());
            ps.setString(4, s.getPhone());
            ps.setString(5, s.getEmail());
            ps.setBigDecimal(6, s.getSalary());
            ps.setDate(7, s.getJoinDate());
            ps.setString(8, s.getStatus());
            ps.setInt(9, s.getStaffId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteStaff(int id) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("DELETE FROM staff WHERE staff_id=?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public int countActiveStaff() throws SQLException {
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM staff WHERE status='Active'")) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    private Staff mapRow(ResultSet rs) throws SQLException {
        Staff s = new Staff();
        s.setStaffId(rs.getInt("staff_id"));
        s.setFullName(rs.getString("full_name"));
        s.setRole(rs.getString("role"));
        s.setDepartment(rs.getString("department"));
        s.setPhone(rs.getString("phone"));
        s.setEmail(rs.getString("email"));
        s.setSalary(rs.getBigDecimal("salary"));
        s.setJoinDate(rs.getDate("join_date"));
        s.setStatus(rs.getString("status"));
        s.setCreatedAt(rs.getTimestamp("created_at"));
        return s;
    }
}
