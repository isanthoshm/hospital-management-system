package com.hospital.servlet;

import com.hospital.dao.AppointmentDAO;
import com.hospital.model.Appointment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.Date;
import java.sql.Time;
import java.util.Map;

public class AppointmentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final AppointmentDAO dao = new AppointmentDAO();
    private final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json"); res.setCharacterEncoding("UTF-8");
        try {
            String path = req.getPathInfo();
            if (path != null && path.length() > 1)
                res.getWriter().print(gson.toJson(dao.getAppointmentById(Integer.parseInt(path.substring(1)))));
            else
                res.getWriter().print(gson.toJson(dao.getAllAppointments()));
        } catch (Exception e) { res.setStatus(500); res.getWriter().print(gson.toJson(Map.of("error", e.getMessage()))); e.printStackTrace(); }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        res.setContentType("application/json"); res.setCharacterEncoding("UTF-8");
        try {
            Appointment a = parse(req);
            res.getWriter().print(gson.toJson(Map.of("success", dao.addAppointment(a))));
        } catch (Exception e) { res.setStatus(500); res.getWriter().print(gson.toJson(Map.of("error", e.getMessage()))); e.printStackTrace(); }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json"); res.setCharacterEncoding("UTF-8");
        try {
            int id = Integer.parseInt(req.getPathInfo().substring(1));
            res.getWriter().print(gson.toJson(Map.of("success", dao.deleteAppointment(id))));
        } catch (Exception e) { res.setStatus(500); res.getWriter().print(gson.toJson(Map.of("error", e.getMessage()))); e.printStackTrace(); }
    }

    private Appointment parse(HttpServletRequest req) {
        Appointment a = new Appointment();
        String pid = req.getParameter("patientId");
        String did = req.getParameter("doctorId");
        if (pid != null && !pid.isBlank()) a.setPatientId(Integer.parseInt(pid));
        if (did != null && !did.isBlank()) a.setDoctorId(Integer.parseInt(did));
        String d = req.getParameter("appointmentDate");
        if (d != null && !d.isBlank()) a.setAppointmentDate(Date.valueOf(d));
        String t = req.getParameter("appointmentTime");
        if (t != null && !t.isBlank()) a.setAppointmentTime(Time.valueOf(t.length() == 5 ? t + ":00" : t));
        a.setReason(req.getParameter("reason"));
        a.setStatus("Scheduled");
        a.setNotes(req.getParameter("notes"));
        return a;
    }
}
