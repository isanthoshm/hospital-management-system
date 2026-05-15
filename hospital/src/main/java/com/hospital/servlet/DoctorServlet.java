package com.hospital.servlet;

import com.hospital.dao.DoctorDAO;
import com.hospital.model.Doctor;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.*;
import java.math.BigDecimal;
import java.util.Map;

public class DoctorServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final DoctorDAO dao = new DoctorDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json"); res.setCharacterEncoding("UTF-8");
        try {
            String path = req.getPathInfo();
            if (path != null && path.length() > 1)
                res.getWriter().print(gson.toJson(dao.getDoctorById(Integer.parseInt(path.substring(1)))));
            else
                res.getWriter().print(gson.toJson(dao.getAllDoctors()));
        } catch (Exception e) { res.setStatus(500); res.getWriter().print(gson.toJson(Map.of("error", e.getMessage()))); e.printStackTrace(); }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        res.setContentType("application/json"); res.setCharacterEncoding("UTF-8");
        try { res.getWriter().print(gson.toJson(Map.of("success", dao.addDoctor(parse(req))))); }
        catch (Exception e) { res.setStatus(500); res.getWriter().print(gson.toJson(Map.of("error", e.getMessage()))); e.printStackTrace(); }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        res.setContentType("application/json"); res.setCharacterEncoding("UTF-8");
        try {
            int id = Integer.parseInt(req.getPathInfo().substring(1));
            Doctor d = parse(req); d.setDoctorId(id);
            res.getWriter().print(gson.toJson(Map.of("success", dao.updateDoctor(d))));
        } catch (Exception e) { res.setStatus(500); res.getWriter().print(gson.toJson(Map.of("error", e.getMessage()))); e.printStackTrace(); }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json"); res.setCharacterEncoding("UTF-8");
        try { res.getWriter().print(gson.toJson(Map.of("success", dao.deleteDoctor(Integer.parseInt(req.getPathInfo().substring(1)))))); }
        catch (Exception e) { res.setStatus(500); res.getWriter().print(gson.toJson(Map.of("error", e.getMessage()))); e.printStackTrace(); }
    }

    private Doctor parse(HttpServletRequest req) {
        Doctor d = new Doctor();
        d.setFullName(req.getParameter("fullName"));
        d.setSpecialization(req.getParameter("specialization"));
        d.setPhone(req.getParameter("phone"));
        d.setEmail(req.getParameter("email"));
        d.setQualification(req.getParameter("qualification"));
        String exp = req.getParameter("experienceYrs");
        d.setExperienceYrs(exp != null && !exp.isBlank() ? Integer.parseInt(exp) : 0);
        String fee = req.getParameter("consultationFee");
        d.setConsultationFee(fee != null && !fee.isBlank() ? new BigDecimal(fee) : BigDecimal.ZERO);
        d.setAvailability(req.getParameter("availability") != null ? req.getParameter("availability") : "Available");
        return d;
    }
}
