package com.hospital.dao;

import com.hospital.model.Room;
import com.hospital.util.DBConnection;
import java.sql.*;
import java.util.*;

public class RoomDAO {

    public boolean addRoom(Room r) throws SQLException {
        String sql = "INSERT INTO rooms (room_number, room_type, floor, capacity, occupied, price_per_day, status) VALUES (?,?,?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, r.getRoomNumber());
            ps.setString(2, r.getRoomType());
            ps.setInt(3, r.getFloor());
            ps.setInt(4, r.getCapacity());
            ps.setInt(5, r.getOccupied());
            ps.setBigDecimal(6, r.getPricePerDay());
            ps.setString(7, r.getStatus());
            return ps.executeUpdate() > 0;
        }
    }

    public List<Room> getAllRooms() throws SQLException {
        List<Room> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM rooms ORDER BY room_number")) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public Room getRoomById(int id) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM rooms WHERE room_id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    public boolean updateRoom(Room r) throws SQLException {
        String sql = "UPDATE rooms SET room_number=?, room_type=?, floor=?, capacity=?, occupied=?, price_per_day=?, status=? WHERE room_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, r.getRoomNumber());
            ps.setString(2, r.getRoomType());
            ps.setInt(3, r.getFloor());
            ps.setInt(4, r.getCapacity());
            ps.setInt(5, r.getOccupied());
            ps.setBigDecimal(6, r.getPricePerDay());
            ps.setString(7, r.getStatus());
            ps.setInt(8, r.getRoomId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteRoom(int id) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("DELETE FROM rooms WHERE room_id = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public int countAvailableRooms() throws SQLException {
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM rooms WHERE status='Available'")) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    private Room mapRow(ResultSet rs) throws SQLException {
        Room r = new Room();
        r.setRoomId(rs.getInt("room_id"));
        r.setRoomNumber(rs.getString("room_number"));
        r.setRoomType(rs.getString("room_type"));
        r.setFloor(rs.getInt("floor"));
        r.setCapacity(rs.getInt("capacity"));
        r.setOccupied(rs.getInt("occupied"));
        r.setPricePerDay(rs.getBigDecimal("price_per_day"));
        r.setStatus(rs.getString("status"));
        r.setCreatedAt(rs.getTimestamp("created_at"));
        return r;
    }
}
