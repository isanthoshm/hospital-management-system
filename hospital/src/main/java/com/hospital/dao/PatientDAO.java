package com.hospital.dao;

import com.hospital.model.Patient;
import com.hospital.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientDAO {

    // ── CREATE ──────────────────────────────────────────────
    public boolean addPatient(Patient p) throws SQLException {
        String sql = "INSERT INTO patients (full_name, dob, gender, blood_group, phone, email, " +
                     "address, emergency_contact, emergency_phone, status) VALUES (?,?,?,?,?,?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getFullName());

            // Handle nullable date
            if (p.getDob() != null)
                ps.setDate(2, p.getDob());
            else
                ps.setNull(2, Types.DATE);

            ps.setString(3, nvl(p.getGender()));
            ps.setString(4, nvl(p.getBloodGroup()));
            ps.setString(5, nvl(p.getPhone()));
            ps.setString(6, nvl(p.getEmail()));
            ps.setString(7, nvl(p.getAddress()));
            ps.setString(8, nvl(p.getEmergencyContact()));
            ps.setString(9, nvl(p.getEmergencyPhone()));
            ps.setString(10, p.getStatus() != null ? p.getStatus() : "Outpatient");

            return ps.executeUpdate() > 0;
        }
    }

    // ── READ ALL ────────────────────────────────────────────
    public List<Patient> getAllPatients() throws SQLException {
        List<Patient> list = new ArrayList<>();
        String sql = "SELECT * FROM patients ORDER BY created_at DESC";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    // ── READ BY ID ──────────────────────────────────────────
    public Patient getPatientById(int id) throws SQLException {
        String sql = "SELECT * FROM patients WHERE patient_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    // ── SEARCH ──────────────────────────────────────────────
    public List<Patient> searchPatients(String keyword) throws SQLException {
        List<Patient> list = new ArrayList<>();
        String sql = "SELECT * FROM patients WHERE full_name LIKE ? OR phone LIKE ? OR email LIKE ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            String kw = "%" + keyword + "%";
            ps.setString(1, kw);
            ps.setString(2, kw);
            ps.setString(3, kw);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        }
        return list;
    }

    // ── UPDATE ──────────────────────────────────────────────
    public boolean updatePatient(Patient p) throws SQLException {
        String sql = "UPDATE patients SET full_name=?, dob=?, gender=?, blood_group=?, phone=?, email=?, " +
                     "address=?, emergency_contact=?, emergency_phone=?, status=? WHERE patient_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getFullName());

            if (p.getDob() != null)
                ps.setDate(2, p.getDob());
            else
                ps.setNull(2, Types.DATE);

            ps.setString(3, nvl(p.getGender()));
            ps.setString(4, nvl(p.getBloodGroup()));
            ps.setString(5, nvl(p.getPhone()));
            ps.setString(6, nvl(p.getEmail()));
            ps.setString(7, nvl(p.getAddress()));
            ps.setString(8, nvl(p.getEmergencyContact()));
            ps.setString(9, nvl(p.getEmergencyPhone()));
            ps.setString(10, p.getStatus() != null ? p.getStatus() : "Outpatient");
            ps.setInt(11, p.getPatientId());

            return ps.executeUpdate() > 0;
        }
    }

    // ── DELETE ──────────────────────────────────────────────
    public boolean deletePatient(int id) throws SQLException {
        String sql = "DELETE FROM patients WHERE patient_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    // ── COUNT ───────────────────────────────────────────────
    public int countPatients() throws SQLException {
        String sql = "SELECT COUNT(*) FROM patients";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    // ── MAPPER ──────────────────────────────────────────────
    private Patient mapRow(ResultSet rs) throws SQLException {
        Patient p = new Patient();
        p.setPatientId(rs.getInt("patient_id"));
        p.setFullName(rs.getString("full_name"));
        p.setDob(rs.getDate("dob"));
        p.setGender(rs.getString("gender"));
        p.setBloodGroup(rs.getString("blood_group"));
        p.setPhone(rs.getString("phone"));
        p.setEmail(rs.getString("email"));
        p.setAddress(rs.getString("address"));
        p.setEmergencyContact(rs.getString("emergency_contact"));
        p.setEmergencyPhone(rs.getString("emergency_phone"));
        p.setRoomId((Integer) rs.getObject("room_id"));
        p.setAdmittedOn(rs.getDate("admitted_on"));
        p.setDischargedOn(rs.getDate("discharged_on"));
        p.setStatus(rs.getString("status"));
        p.setCreatedAt(rs.getTimestamp("created_at"));
        return p;
    }

    // ── NULL HELPER ─────────────────────────────────────────
    private String nvl(String s) {
        return (s == null || s.isBlank()) ? null : s;
    }
}
