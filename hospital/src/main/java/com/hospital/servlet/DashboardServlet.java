package com.hospital.servlet;

import com.hospital.dao.*;
import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.*;
import java.util.*;

public class DashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final PatientDAO     patientDAO     = new PatientDAO();
    private final DoctorDAO      doctorDAO      = new DoctorDAO();
    private final AppointmentDAO appointmentDAO = new AppointmentDAO();
    private final RoomDAO        roomDAO        = new RoomDAO();
    private final StaffDAO       staffDAO       = new StaffDAO();
    private final MedicineDAO    medicineDAO    = new MedicineDAO();
    private final LabReportDAO   labDAO         = new LabReportDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        PrintWriter out = res.getWriter();

        try {
            Map<String, Object> stats = new LinkedHashMap<>();
            stats.put("totalPatients",       patientDAO.countPatients());
            stats.put("totalDoctors",        doctorDAO.countDoctors());
            stats.put("todayAppointments",   appointmentDAO.countTodayAppointments());
            stats.put("availableRooms",      roomDAO.countAvailableRooms());
            stats.put("activeStaff",         staffDAO.countActiveStaff());
            stats.put("lowStockMedicines",   medicineDAO.countLowStock(20));
            stats.put("pendingLabReports",   labDAO.countPendingReports());
            out.print(gson.toJson(stats));
        } catch (Exception e) {
            res.setStatus(500);
            out.print(gson.toJson(Map.of("error", e.getMessage())));
        }
    }
}
