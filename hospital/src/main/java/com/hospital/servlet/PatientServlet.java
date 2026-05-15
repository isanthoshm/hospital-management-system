package com.hospital.servlet;

import com.hospital.dao.PatientDAO;
import com.hospital.model.Patient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.Date;
import java.util.*;

public class PatientServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final PatientDAO dao = new PatientDAO();
    private final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        PrintWriter out = res.getWriter();
        try {
            String path    = req.getPathInfo();
            String keyword = req.getParameter("search");
            if (path != null && path.length() > 1) {
                int id = Integer.parseInt(path.substring(1));
                Patient p = dao.getPatientById(id);
                out.print(gson.toJson(p != null ? p : Map.of("error", "Not found")));
            } else if (keyword != null && !keyword.isBlank()) {
                out.print(gson.toJson(dao.searchPatients(keyword)));
            } else {
                out.print(gson.toJson(dao.getAllPatients()));
            }
        } catch (Exception e) {
            res.setStatus(500);
            out.print(gson.toJson(Map.of("error", e.getMessage())));
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        PrintWriter out = res.getWriter();
        try {
            // Log all params received
            System.out.println("=== ADD PATIENT PARAMS ===");
            req.getParameterMap().forEach((k, v) ->
                System.out.println(k + " = " + Arrays.toString(v)));

            Patient p = parseFromParams(req);
            boolean ok = dao.addPatient(p);
            System.out.println("Patient added: " + ok);
            out.print(gson.toJson(Map.of("success", ok,
                                         "message", ok ? "Patient added successfully" : "Failed to add")));
        } catch (Exception e) {
            res.setStatus(500);
            out.print(gson.toJson(Map.of("error", e.getMessage())));
            e.printStackTrace();
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        PrintWriter out = res.getWriter();
        try {
            int id = Integer.parseInt(req.getPathInfo().substring(1));
            Patient p = parseFromParams(req);
            p.setPatientId(id);
            boolean ok = dao.updatePatient(p);
            out.print(gson.toJson(Map.of("success", ok)));
        } catch (Exception e) {
            res.setStatus(500);
            out.print(gson.toJson(Map.of("error", e.getMessage())));
            e.printStackTrace();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        PrintWriter out = res.getWriter();
        try {
            int id = Integer.parseInt(req.getPathInfo().substring(1));
            boolean ok = dao.deletePatient(id);
            out.print(gson.toJson(Map.of("success", ok)));
        } catch (Exception e) {
            res.setStatus(500);
            out.print(gson.toJson(Map.of("error", e.getMessage())));
            e.printStackTrace();
        }
    }

    private Patient parseFromParams(HttpServletRequest req) {
        Patient p = new Patient();
        p.setFullName(req.getParameter("fullName"));

        String dob = req.getParameter("dob");
        if (dob != null && !dob.isBlank()) {
            try { p.setDob(Date.valueOf(dob)); }
            catch (Exception e) { System.out.println("Invalid dob: " + dob); }
        }

        p.setGender(req.getParameter("gender"));
        p.setBloodGroup(req.getParameter("bloodGroup"));
        p.setPhone(req.getParameter("phone"));
        p.setEmail(req.getParameter("email"));
        p.setAddress(req.getParameter("address"));
        p.setEmergencyContact(req.getParameter("emergencyContact"));
        p.setEmergencyPhone(req.getParameter("emergencyPhone"));
        String status = req.getParameter("status");
        p.setStatus(status != null && !status.isBlank() ? status : "Outpatient");
        return p;
    }
}
