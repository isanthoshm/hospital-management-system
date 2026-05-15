package com.hospital.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Room {
    private int roomId;
    private String roomNumber;
    private String roomType;
    private int floor;
    private int capacity;
    private int occupied;
    private BigDecimal pricePerDay;
    private String status;
    private Timestamp createdAt;

    public Room() {}

    public int getRoomId()                  { return roomId; }
    public void setRoomId(int id)           { this.roomId = id; }

    public String getRoomNumber()           { return roomNumber; }
    public void setRoomNumber(String n)     { this.roomNumber = n; }

    public String getRoomType()             { return roomType; }
    public void setRoomType(String t)       { this.roomType = t; }

    public int getFloor()                   { return floor; }
    public void setFloor(int f)             { this.floor = f; }

    public int getCapacity()                { return capacity; }
    public void setCapacity(int c)          { this.capacity = c; }

    public int getOccupied()                { return occupied; }
    public void setOccupied(int o)          { this.occupied = o; }

    public BigDecimal getPricePerDay()      { return pricePerDay; }
    public void setPricePerDay(BigDecimal p){ this.pricePerDay = p; }

    public String getStatus()               { return status; }
    public void setStatus(String s)         { this.status = s; }

    public Timestamp getCreatedAt()         { return createdAt; }
    public void setCreatedAt(Timestamp t)   { this.createdAt = t; }
}
