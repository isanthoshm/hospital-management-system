package com.hospital.dao;

import com.hospital.model.LabReport;
import com.hospital.util.DBConnection;
import java.sql.*;
import java.util.*;

public class LabReportDAO {

    public boolean addReport(LabReport lr) throws SQLException {
        String sql = "INSERT INTO lab_reports (patient_id, doctor_id, test_name, test_date, result, normal_range, status, remarks) VALUES (?,?,?,?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, lr.getPatientId());
            ps.setInt(2, lr.getDoctorId());
            ps.setString(3, lr.getTestName());
            ps.setDate(4, lr.getTestDate());
            ps.setString(5, lr.getResult());
            ps.setString(6, lr.getNormalRange());
            ps.setString(7, lr.getStatus() != null ? lr.getStatus() : "Pending");
            ps.setString(8, lr.getRemarks());
            return ps.executeUpdate() > 0;
        }
    }

    public List<LabReport> getAllReports() throws SQLException {
        List<LabReport> list = new ArrayList<>();
        String sql = "SELECT lr.*, p.full_name AS patient_name, d.full_name AS doctor_name " +
                     "FROM lab_reports lr " +
                     "JOIN patients p ON lr.patient_id=p.patient_id " +
                     "JOIN doctors  d ON lr.doctor_id =d.doctor_id " +
                     "ORDER BY lr.test_date DESC";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public LabReport getReportById(int id) throws SQLException {
        String sql = "SELECT lr.*, p.full_name AS patient_name, d.full_name AS doctor_name " +
                     "FROM lab_reports lr " +
                     "JOIN patients p ON lr.patient_id=p.patient_id " +
                     "JOIN doctors  d ON lr.doctor_id =d.doctor_id " +
                     "WHERE lr.report_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    public boolean updateReport(LabReport lr) throws SQLException {
        String sql = "UPDATE lab_reports SET result=?, normal_range=?, status=?, remarks=? WHERE report_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, lr.getResult());
            ps.setString(2, lr.getNormalRange());
            ps.setString(3, lr.getStatus());
            ps.setString(4, lr.getRemarks());
            ps.setInt(5, lr.getReportId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteReport(int id) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("DELETE FROM lab_reports WHERE report_id=?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public int countPendingReports() throws SQLException {
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM lab_reports WHERE status='Pending'")) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    private LabReport mapRow(ResultSet rs) throws SQLException {
        LabReport lr = new LabReport();
        lr.setReportId(rs.getInt("report_id"));
        lr.setPatientId(rs.getInt("patient_id"));
        lr.setDoctorId(rs.getInt("doctor_id"));
        lr.setTestName(rs.getString("test_name"));
        lr.setTestDate(rs.getDate("test_date"));
        lr.setResult(rs.getString("result"));
        lr.setNormalRange(rs.getString("normal_range"));
        lr.setStatus(rs.getString("status"));
        lr.setRemarks(rs.getString("remarks"));
        lr.setCreatedAt(rs.getTimestamp("created_at"));
        try { lr.setPatientName(rs.getString("patient_name")); } catch (Exception ignored) {}
        try { lr.setDoctorName(rs.getString("doctor_name")); } catch (Exception ignored) {}
        return lr;
    }
}
