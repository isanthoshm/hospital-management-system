package com.hospital.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

public class Staff {
    private int staffId;
    private String fullName;
    private String role;
    private String department;
    private String phone;
    private String email;
    private BigDecimal salary;
    private Date joinDate;
    private String status;
    private Timestamp createdAt;

    public Staff() {}

    public int getStaffId()                 { return staffId; }
    public void setStaffId(int id)          { this.staffId = id; }

    public String getFullName()             { return fullName; }
    public void setFullName(String n)       { this.fullName = n; }

    public String getRole()                 { return role; }
    public void setRole(String r)           { this.role = r; }

    public String getDepartment()           { return department; }
    public void setDepartment(String d)     { this.department = d; }

    public String getPhone()                { return phone; }
    public void setPhone(String p)          { this.phone = p; }

    public String getEmail()                { return email; }
    public void setEmail(String e)          { this.email = e; }

    public BigDecimal getSalary()           { return salary; }
    public void setSalary(BigDecimal s)     { this.salary = s; }

    public Date getJoinDate()               { return joinDate; }
    public void setJoinDate(Date d)         { this.joinDate = d; }

    public String getStatus()               { return status; }
    public void setStatus(String s)         { this.status = s; }

    public Timestamp getCreatedAt()         { return createdAt; }
    public void setCreatedAt(Timestamp t)   { this.createdAt = t; }
}
