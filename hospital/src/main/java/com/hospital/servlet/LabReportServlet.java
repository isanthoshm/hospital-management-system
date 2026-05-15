package com.hospital.servlet;

import com.hospital.dao.LabReportDAO;
import com.hospital.model.LabReport;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.Date;
import java.util.Map;

public class LabReportServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final LabReportDAO dao = new LabReportDAO();
    private final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json"); res.setCharacterEncoding("UTF-8");
        try {
            String path = req.getPathInfo();
            if (path != null && path.length() > 1)
                res.getWriter().print(gson.toJson(dao.getReportById(Integer.parseInt(path.substring(1)))));
            else
                res.getWriter().print(gson.toJson(dao.getAllReports()));
        } catch (Exception e) { res.setStatus(500); res.getWriter().print(gson.toJson(Map.of("error", e.getMessage()))); e.printStackTrace(); }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        res.setContentType("application/json"); res.setCharacterEncoding("UTF-8");
        try { res.getWriter().print(gson.toJson(Map.of("success", dao.addReport(parse(req))))); }
        catch (Exception e) { res.setStatus(500); res.getWriter().print(gson.toJson(Map.of("error", e.getMessage()))); e.printStackTrace(); }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        res.setContentType("application/json"); res.setCharacterEncoding("UTF-8");
        try {
            LabReport lr = parse(req); lr.setReportId(Integer.parseInt(req.getPathInfo().substring(1)));
            res.getWriter().print(gson.toJson(Map.of("success", dao.updateReport(lr))));
        } catch (Exception e) { res.setStatus(500); res.getWriter().print(gson.toJson(Map.of("error", e.getMessage()))); e.printStackTrace(); }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json"); res.setCharacterEncoding("UTF-8");
        try { res.getWriter().print(gson.toJson(Map.of("success", dao.deleteReport(Integer.parseInt(req.getPathInfo().substring(1)))))); }
        catch (Exception e) { res.setStatus(500); res.getWriter().print(gson.toJson(Map.of("error", e.getMessage()))); e.printStackTrace(); }
    }

    private LabReport parse(HttpServletRequest req) {
        LabReport lr = new LabReport();
        String pid = req.getParameter("patientId"); if (pid != null && !pid.isBlank()) lr.setPatientId(Integer.parseInt(pid));
        String did = req.getParameter("doctorId");  if (did != null && !did.isBlank()) lr.setDoctorId(Integer.parseInt(did));
        lr.setTestName(req.getParameter("testName"));
        String td = req.getParameter("testDate"); if (td != null && !td.isBlank()) lr.setTestDate(Date.valueOf(td));
        lr.setResult(req.getParameter("result"));
        lr.setNormalRange(req.getParameter("normalRange"));
        lr.setStatus(req.getParameter("status") != null ? req.getParameter("status") : "Pending");
        lr.setRemarks(req.getParameter("remarks"));
        return lr;
    }
}
