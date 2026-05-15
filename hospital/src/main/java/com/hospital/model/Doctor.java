package com.hospital.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Doctor {
    private int doctorId;
    private String fullName;
    private String specialization;
    private String phone;
    private String email;
    private String qualification;
    private int experienceYrs;
    private BigDecimal consultationFee;
    private String availability;
    private Timestamp createdAt;

    public Doctor() {}

    public Doctor(String fullName, String specialization, String phone, String email,
                  String qualification, int experienceYrs, BigDecimal consultationFee, String availability) {
        this.fullName = fullName;
        this.specialization = specialization;
        this.phone = phone;
        this.email = email;
        this.qualification = qualification;
        this.experienceYrs = experienceYrs;
        this.consultationFee = consultationFee;
        this.availability = availability;
    }

    public int getDoctorId()                    { return doctorId; }
    public void setDoctorId(int id)             { this.doctorId = id; }

    public String getFullName()                 { return fullName; }
    public void setFullName(String n)           { this.fullName = n; }

    public String getSpecialization()           { return specialization; }
    public void setSpecialization(String s)     { this.specialization = s; }

    public String getPhone()                    { return phone; }
    public void setPhone(String p)              { this.phone = p; }

    public String getEmail()                    { return email; }
    public void setEmail(String e)              { this.email = e; }

    public String getQualification()            { return qualification; }
    public void setQualification(String q)      { this.qualification = q; }

    public int getExperienceYrs()               { return experienceYrs; }
    public void setExperienceYrs(int y)         { this.experienceYrs = y; }

    public BigDecimal getConsultationFee()      { return consultationFee; }
    public void setConsultationFee(BigDecimal f){ this.consultationFee = f; }

    public String getAvailability()             { return availability; }
    public void setAvailability(String a)       { this.availability = a; }

    public Timestamp getCreatedAt()             { return createdAt; }
    public void setCreatedAt(Timestamp t)       { this.createdAt = t; }
}
