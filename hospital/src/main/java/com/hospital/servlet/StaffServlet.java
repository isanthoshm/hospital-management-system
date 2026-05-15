package com.hospital.servlet;

import com.hospital.dao.StaffDAO;
import com.hospital.model.Staff;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Map;

public class StaffServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final StaffDAO dao = new StaffDAO();
    private final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json"); res.setCharacterEncoding("UTF-8");
        try {
            String path = req.getPathInfo();
            if (path != null && path.length() > 1)
                res.getWriter().print(gson.toJson(dao.getStaffById(Integer.parseInt(path.substring(1)))));
            else
                res.getWriter().print(gson.toJson(dao.getAllStaff()));
        } catch (Exception e) { res.setStatus(500); res.getWriter().print(gson.toJson(Map.of("error", e.getMessage()))); e.printStackTrace(); }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        res.setContentType("application/json"); res.setCharacterEncoding("UTF-8");
        try { res.getWriter().print(gson.toJson(Map.of("success", dao.addStaff(parse(req))))); }
        catch (Exception e) { res.setStatus(500); res.getWriter().print(gson.toJson(Map.of("error", e.getMessage()))); e.printStackTrace(); }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        res.setContentType("application/json"); res.setCharacterEncoding("UTF-8");
        try {
            Staff s = parse(req); s.setStaffId(Integer.parseInt(req.getPathInfo().substring(1)));
            res.getWriter().print(gson.toJson(Map.of("success", dao.updateStaff(s))));
        } catch (Exception e) { res.setStatus(500); res.getWriter().print(gson.toJson(Map.of("error", e.getMessage()))); e.printStackTrace(); }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json"); res.setCharacterEncoding("UTF-8");
        try { res.getWriter().print(gson.toJson(Map.of("success", dao.deleteStaff(Integer.parseInt(req.getPathInfo().substring(1)))))); }
        catch (Exception e) { res.setStatus(500); res.getWriter().print(gson.toJson(Map.of("error", e.getMessage()))); e.printStackTrace(); }
    }

    private Staff parse(HttpServletRequest req) {
        Staff s = new Staff();
        s.setFullName(req.getParameter("fullName"));
        s.setRole(req.getParameter("role"));
        s.setDepartment(req.getParameter("department"));
        s.setPhone(req.getParameter("phone"));
        s.setEmail(req.getParameter("email"));
        String sal = req.getParameter("salary"); s.setSalary(sal != null && !sal.isBlank() ? new BigDecimal(sal) : BigDecimal.ZERO);
        String jd = req.getParameter("joinDate"); if (jd != null && !jd.isBlank()) s.setJoinDate(Date.valueOf(jd));
        s.setStatus(req.getParameter("status") != null ? req.getParameter("status") : "Active");
        return s;
    }
}
