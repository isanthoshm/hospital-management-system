package com.hospital.dao;

import com.hospital.model.Doctor;
import com.hospital.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DoctorDAO {

    public boolean addDoctor(Doctor d) throws SQLException {
        String sql = "INSERT INTO doctors (full_name, specialization, phone, email, qualification, experience_yrs, consultation_fee, availability) VALUES (?,?,?,?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, d.getFullName());
            ps.setString(2, d.getSpecialization());
            ps.setString(3, d.getPhone());
            ps.setString(4, d.getEmail());
            ps.setString(5, d.getQualification());
            ps.setInt(6, d.getExperienceYrs());
            ps.setBigDecimal(7, d.getConsultationFee());
            ps.setString(8, d.getAvailability());
            return ps.executeUpdate() > 0;
        }
    }

    public List<Doctor> getAllDoctors() throws SQLException {
        List<Doctor> list = new ArrayList<>();
        String sql = "SELECT * FROM doctors ORDER BY full_name";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public Doctor getDoctorById(int id) throws SQLException {
        String sql = "SELECT * FROM doctors WHERE doctor_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    public boolean updateDoctor(Doctor d) throws SQLException {
        String sql = "UPDATE doctors SET full_name=?, specialization=?, phone=?, email=?, qualification=?, experience_yrs=?, consultation_fee=?, availability=? WHERE doctor_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, d.getFullName());
            ps.setString(2, d.getSpecialization());
            ps.setString(3, d.getPhone());
            ps.setString(4, d.getEmail());
            ps.setString(5, d.getQualification());
            ps.setInt(6, d.getExperienceYrs());
            ps.setBigDecimal(7, d.getConsultationFee());
            ps.setString(8, d.getAvailability());
            ps.setInt(9, d.getDoctorId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteDoctor(int id) throws SQLException {
        String sql = "DELETE FROM doctors WHERE doctor_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public int countDoctors() throws SQLException {
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM doctors")) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    private Doctor mapRow(ResultSet rs) throws SQLException {
        Doctor d = new Doctor();
        d.setDoctorId(rs.getInt("doctor_id"));
        d.setFullName(rs.getString("full_name"));
        d.setSpecialization(rs.getString("specialization"));
        d.setPhone(rs.getString("phone"));
        d.setEmail(rs.getString("email"));
        d.setQualification(rs.getString("qualification"));
        d.setExperienceYrs(rs.getInt("experience_yrs"));
        d.setConsultationFee(rs.getBigDecimal("consultation_fee"));
        d.setAvailability(rs.getString("availability"));
        d.setCreatedAt(rs.getTimestamp("created_at"));
        return d;
    }
}
