package com.hospital.servlet;

import com.hospital.dao.MedicineDAO;
import com.hospital.model.Medicine;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Map;

public class MedicineServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final MedicineDAO dao = new MedicineDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json"); res.setCharacterEncoding("UTF-8");
        try {
            String path = req.getPathInfo();
            if (path != null && path.length() > 1)
                res.getWriter().print(gson.toJson(dao.getMedicineById(Integer.parseInt(path.substring(1)))));
            else
                res.getWriter().print(gson.toJson(dao.getAllMedicines()));
        } catch (Exception e) { res.setStatus(500); res.getWriter().print(gson.toJson(Map.of("error", e.getMessage()))); e.printStackTrace(); }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        res.setContentType("application/json"); res.setCharacterEncoding("UTF-8");
        try { res.getWriter().print(gson.toJson(Map.of("success", dao.addMedicine(parse(req))))); }
        catch (Exception e) { res.setStatus(500); res.getWriter().print(gson.toJson(Map.of("error", e.getMessage()))); e.printStackTrace(); }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        res.setContentType("application/json"); res.setCharacterEncoding("UTF-8");
        try {
            Medicine m = parse(req); m.setMedicineId(Integer.parseInt(req.getPathInfo().substring(1)));
            res.getWriter().print(gson.toJson(Map.of("success", dao.updateMedicine(m))));
        } catch (Exception e) { res.setStatus(500); res.getWriter().print(gson.toJson(Map.of("error", e.getMessage()))); e.printStackTrace(); }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json"); res.setCharacterEncoding("UTF-8");
        try { res.getWriter().print(gson.toJson(Map.of("success", dao.deleteMedicine(Integer.parseInt(req.getPathInfo().substring(1)))))); }
        catch (Exception e) { res.setStatus(500); res.getWriter().print(gson.toJson(Map.of("error", e.getMessage()))); e.printStackTrace(); }
    }

    private Medicine parse(HttpServletRequest req) {
        Medicine m = new Medicine();
        m.setName(req.getParameter("name"));
        m.setCategory(req.getParameter("category"));
        m.setManufacturer(req.getParameter("manufacturer"));
        String p = req.getParameter("unitPrice"); m.setUnitPrice(p != null && !p.isBlank() ? new BigDecimal(p) : BigDecimal.ZERO);
        String q = req.getParameter("stockQty"); m.setStockQty(q != null && !q.isBlank() ? Integer.parseInt(q) : 0);
        String exp = req.getParameter("expiryDate"); if (exp != null && !exp.isBlank()) m.setExpiryDate(Date.valueOf(exp));
        return m;
    }
}
