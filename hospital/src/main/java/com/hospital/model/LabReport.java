package com.hospital.model;

import java.sql.Date;
import java.sql.Timestamp;

public class LabReport {
    private int reportId;
    private int patientId;
    private int doctorId;
    private String testName;
    private Date testDate;
    private String result;
    private String normalRange;
    private String status;
    private String remarks;
    private Timestamp createdAt;

    // JOIN fields
    private String patientName;
    private String doctorName;

    public LabReport() {}

    public int getReportId()                { return reportId; }
    public void setReportId(int id)         { this.reportId = id; }

    public int getPatientId()               { return patientId; }
    public void setPatientId(int id)        { this.patientId = id; }

    public int getDoctorId()                { return doctorId; }
    public void setDoctorId(int id)         { this.doctorId = id; }

    public String getTestName()             { return testName; }
    public void setTestName(String n)       { this.testName = n; }

    public Date getTestDate()               { return testDate; }
    public void setTestDate(Date d)         { this.testDate = d; }

    public String getResult()               { return result; }
    public void setResult(String r)         { this.result = r; }

    public String getNormalRange()          { return normalRange; }
    public void setNormalRange(String n)    { this.normalRange = n; }

    public String getStatus()               { return status; }
    public void setStatus(String s)         { this.status = s; }

    public String getRemarks()              { return remarks; }
    public void setRemarks(String r)        { this.remarks = r; }

    public Timestamp getCreatedAt()         { return createdAt; }
    public void setCreatedAt(Timestamp t)   { this.createdAt = t; }

    public String getPatientName()          { return patientName; }
    public void setPatientName(String n)    { this.patientName = n; }

    public String getDoctorName()           { return doctorName; }
    public void setDoctorName(String n)     { this.doctorName = n; }
}
