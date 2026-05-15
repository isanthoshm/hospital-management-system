package com.hospital.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

public class Bill {
    private int billId;
    private int patientId;
    private String patientName;
    private Date billDate;
    private BigDecimal consultationFee;
    private BigDecimal roomCharges;
    private BigDecimal medicineCharges;
    private BigDecimal labCharges;
    private BigDecimal otherCharges;
    private BigDecimal discount;
    private BigDecimal totalAmount;
    private BigDecimal paidAmount;
    private String paymentMethod;
    private String status;
    private Timestamp createdAt;

    public Bill() {}

    public int getBillId()                      { return billId; }
    public void setBillId(int id)               { this.billId = id; }

    public int getPatientId()                   { return patientId; }
    public void setPatientId(int id)            { this.patientId = id; }

    public String getPatientName()              { return patientName; }
    public void setPatientName(String n)        { this.patientName = n; }

    public Date getBillDate()                   { return billDate; }
    public void setBillDate(Date d)             { this.billDate = d; }

    public BigDecimal getConsultationFee()      { return consultationFee; }
    public void setConsultationFee(BigDecimal f){ this.consultationFee = f; }

    public BigDecimal getRoomCharges()          { return roomCharges; }
    public void setRoomCharges(BigDecimal r)    { this.roomCharges = r; }

    public BigDecimal getMedicineCharges()      { return medicineCharges; }
    public void setMedicineCharges(BigDecimal m){ this.medicineCharges = m; }

    public BigDecimal getLabCharges()           { return labCharges; }
    public void setLabCharges(BigDecimal l)     { this.labCharges = l; }

    public BigDecimal getOtherCharges()         { return otherCharges; }
    public void setOtherCharges(BigDecimal o)   { this.otherCharges = o; }

    public BigDecimal getDiscount()             { return discount; }
    public void setDiscount(BigDecimal d)       { this.discount = d; }

    public BigDecimal getTotalAmount()          { return totalAmount; }
    public void setTotalAmount(BigDecimal t)    { this.totalAmount = t; }

    public BigDecimal getPaidAmount()           { return paidAmount; }
    public void setPaidAmount(BigDecimal p)     { this.paidAmount = p; }

    public String getPaymentMethod()            { return paymentMethod; }
    public void setPaymentMethod(String m)      { this.paymentMethod = m; }

    public String getStatus()                   { return status; }
    public void setStatus(String s)             { this.status = s; }

    public Timestamp getCreatedAt()             { return createdAt; }
    public void setCreatedAt(Timestamp t)       { this.createdAt = t; }
}
