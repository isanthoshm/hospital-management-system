package com.hospital.model;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

public class Appointment {
    private int appointmentId;
    private int patientId;
    private int doctorId;
    private Date appointmentDate;
    private Time appointmentTime;
    private String reason;
    private String status;
    private String notes;
    private Timestamp createdAt;

    // Extra fields from JOIN
    private String patientName;
    private String doctorName;
    private String doctorSpecialization;

    public Appointment() {}

    public int getAppointmentId()              { return appointmentId; }
    public void setAppointmentId(int id)       { this.appointmentId = id; }

    public int getPatientId()                  { return patientId; }
    public void setPatientId(int id)           { this.patientId = id; }

    public int getDoctorId()                   { return doctorId; }
    public void setDoctorId(int id)            { this.doctorId = id; }

    public Date getAppointmentDate()           { return appointmentDate; }
    public void setAppointmentDate(Date d)     { this.appointmentDate = d; }

    public Time getAppointmentTime()           { return appointmentTime; }
    public void setAppointmentTime(Time t)     { this.appointmentTime = t; }

    public String getReason()                  { return reason; }
    public void setReason(String r)            { this.reason = r; }

    public String getStatus()                  { return status; }
    public void setStatus(String s)            { this.status = s; }

    public String getNotes()                   { return notes; }
    public void setNotes(String n)             { this.notes = n; }

    public Timestamp getCreatedAt()            { return createdAt; }
    public void setCreatedAt(Timestamp t)      { this.createdAt = t; }

    public String getPatientName()             { return patientName; }
    public void setPatientName(String n)       { this.patientName = n; }

    public String getDoctorName()              { return doctorName; }
    public void setDoctorName(String n)        { this.doctorName = n; }

    public String getDoctorSpecialization()    { return doctorSpecialization; }
    public void setDoctorSpecialization(String s) { this.doctorSpecialization = s; }
}
