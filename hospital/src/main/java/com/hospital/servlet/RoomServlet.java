package com.hospital.servlet;

import com.hospital.dao.RoomDAO;
import com.hospital.model.Room;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.*;
import java.math.BigDecimal;
import java.util.Map;

public class RoomServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final RoomDAO dao = new RoomDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json"); res.setCharacterEncoding("UTF-8");
        try {
            String path = req.getPathInfo();
            if (path != null && path.length() > 1)
                res.getWriter().print(gson.toJson(dao.getRoomById(Integer.parseInt(path.substring(1)))));
            else
                res.getWriter().print(gson.toJson(dao.getAllRooms()));
        } catch (Exception e) { res.setStatus(500); res.getWriter().print(gson.toJson(Map.of("error", e.getMessage()))); e.printStackTrace(); }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        res.setContentType("application/json"); res.setCharacterEncoding("UTF-8");
        try { res.getWriter().print(gson.toJson(Map.of("success", dao.addRoom(parse(req))))); }
        catch (Exception e) { res.setStatus(500); res.getWriter().print(gson.toJson(Map.of("error", e.getMessage()))); e.printStackTrace(); }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        res.setContentType("application/json"); res.setCharacterEncoding("UTF-8");
        try {
            Room r = parse(req); r.setRoomId(Integer.parseInt(req.getPathInfo().substring(1)));
            res.getWriter().print(gson.toJson(Map.of("success", dao.updateRoom(r))));
        } catch (Exception e) { res.setStatus(500); res.getWriter().print(gson.toJson(Map.of("error", e.getMessage()))); e.printStackTrace(); }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json"); res.setCharacterEncoding("UTF-8");
        try { res.getWriter().print(gson.toJson(Map.of("success", dao.deleteRoom(Integer.parseInt(req.getPathInfo().substring(1)))))); }
        catch (Exception e) { res.setStatus(500); res.getWriter().print(gson.toJson(Map.of("error", e.getMessage()))); e.printStackTrace(); }
    }

    private Room parse(HttpServletRequest req) {
        Room r = new Room();
        r.setRoomNumber(req.getParameter("roomNumber"));
        r.setRoomType(req.getParameter("roomType"));
        String fl = req.getParameter("floor"); r.setFloor(fl != null && !fl.isBlank() ? Integer.parseInt(fl) : 1);
        String cap = req.getParameter("capacity"); r.setCapacity(cap != null && !cap.isBlank() ? Integer.parseInt(cap) : 1);
        r.setOccupied(0);
        String ppd = req.getParameter("pricePerDay"); r.setPricePerDay(ppd != null && !ppd.isBlank() ? new BigDecimal(ppd) : BigDecimal.ZERO);
        r.setStatus(req.getParameter("status") != null ? req.getParameter("status") : "Available");
        return r;
    }
}
