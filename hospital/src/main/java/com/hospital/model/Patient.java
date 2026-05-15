package com.hospital.model;

import java.sql.Date;
import java.sql.Timestamp;

public class Patient {
    private int patientId;
    private String fullName;
    private Date dob;
    private String gender;
    private String bloodGroup;
    private String phone;
    private String email;
    private String address;
    private String emergencyContact;
    private String emergencyPhone;
    private Integer roomId;
    private Date admittedOn;
    private Date dischargedOn;
    private String status;
    private Timestamp createdAt;

    // Constructors
    public Patient() {}

    public Patient(String fullName, Date dob, String gender, String bloodGroup,
                   String phone, String email, String address,
                   String emergencyContact, String emergencyPhone, String status) {
        this.fullName = fullName;
        this.dob = dob;
        this.gender = gender;
        this.bloodGroup = bloodGroup;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.emergencyContact = emergencyContact;
        this.emergencyPhone = emergencyPhone;
        this.status = status;
    }

    // Getters and Setters
    public int getPatientId()              { return patientId; }
    public void setPatientId(int id)       { this.patientId = id; }

    public String getFullName()            { return fullName; }
    public void setFullName(String n)      { this.fullName = n; }

    public Date getDob()                   { return dob; }
    public void setDob(Date d)             { this.dob = d; }

    public String getGender()              { return gender; }
    public void setGender(String g)        { this.gender = g; }

    public String getBloodGroup()          { return bloodGroup; }
    public void setBloodGroup(String b)    { this.bloodGroup = b; }

    public String getPhone()               { return phone; }
    public void setPhone(String p)         { this.phone = p; }

    public String getEmail()               { return email; }
    public void setEmail(String e)         { this.email = e; }

    public String getAddress()             { return address; }
    public void setAddress(String a)       { this.address = a; }

    public String getEmergencyContact()    { return emergencyContact; }
    public void setEmergencyContact(String ec) { this.emergencyContact = ec; }

    public String getEmergencyPhone()      { return emergencyPhone; }
    public void setEmergencyPhone(String ep) { this.emergencyPhone = ep; }

    public Integer getRoomId()             { return roomId; }
    public void setRoomId(Integer r)       { this.roomId = r; }

    public Date getAdmittedOn()            { return admittedOn; }
    public void setAdmittedOn(Date d)      { this.admittedOn = d; }

    public Date getDischargedOn()          { return dischargedOn; }
    public void setDischargedOn(Date d)    { this.dischargedOn = d; }

    public String getStatus()              { return status; }
    public void setStatus(String s)        { this.status = s; }

    public Timestamp getCreatedAt()        { return createdAt; }
    public void setCreatedAt(Timestamp t)  { this.createdAt = t; }

    @Override
    public String toString() {
        return "Patient{id=" + patientId + ", name='" + fullName + "', status='" + status + "'}";
    }
}
