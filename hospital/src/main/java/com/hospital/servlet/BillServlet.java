package com.hospital.servlet;

import com.hospital.dao.BillDAO;
import com.hospital.model.Bill;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Map;

public class BillServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final BillDAO dao = new BillDAO();
    private final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json"); res.setCharacterEncoding("UTF-8");
        try {
            String path = req.getPathInfo();
            if (path != null && path.length() > 1)
                res.getWriter().print(gson.toJson(dao.getBillById(Integer.parseInt(path.substring(1)))));
            else
                res.getWriter().print(gson.toJson(dao.getAllBills()));
        } catch (Exception e) { res.setStatus(500); res.getWriter().print(gson.toJson(Map.of("error", e.getMessage()))); e.printStackTrace(); }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        res.setContentType("application/json"); res.setCharacterEncoding("UTF-8");
        try { res.getWriter().print(gson.toJson(Map.of("success", dao.addBill(parse(req))))); }
        catch (Exception e) { res.setStatus(500); res.getWriter().print(gson.toJson(Map.of("error", e.getMessage()))); e.printStackTrace(); }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json"); res.setCharacterEncoding("UTF-8");
        try { res.getWriter().print(gson.toJson(Map.of("success", dao.deleteBill(Integer.parseInt(req.getPathInfo().substring(1)))))); }
        catch (Exception e) { res.setStatus(500); res.getWriter().print(gson.toJson(Map.of("error", e.getMessage()))); e.printStackTrace(); }
    }

    private Bill parse(HttpServletRequest req) {
        Bill b = new Bill();
        String pid = req.getParameter("patientId");
        if (pid != null && !pid.isBlank()) b.setPatientId(Integer.parseInt(pid));
        String bd = req.getParameter("billDate");
        if (bd != null && !bd.isBlank()) b.setBillDate(Date.valueOf(bd));
        b.setConsultationFee(getBD(req, "consultationFee"));
        b.setRoomCharges(getBD(req, "roomCharges"));
        b.setMedicineCharges(getBD(req, "medicineCharges"));
        b.setLabCharges(getBD(req, "labCharges"));
        b.setOtherCharges(getBD(req, "otherCharges"));
        b.setDiscount(getBD(req, "discount"));
        b.setTotalAmount(getBD(req, "totalAmount"));
        b.setPaidAmount(getBD(req, "paidAmount"));
        b.setPaymentMethod(req.getParameter("paymentMethod") != null ? req.getParameter("paymentMethod") : "Cash");
        b.setStatus(req.getParameter("status") != null ? req.getParameter("status") : "Pending");
        return b;
    }

    private BigDecimal getBD(HttpServletRequest req, String name) {
        String v = req.getParameter(name);
        return v != null && !v.isBlank() ? new BigDecimal(v) : BigDecimal.ZERO;
    }
}
