package com.hospital.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

public class Medicine {
    private int medicineId;
    private String name;
    private String category;
    private String manufacturer;
    private BigDecimal unitPrice;
    private int stockQty;
    private Date expiryDate;
    private Timestamp createdAt;

    public Medicine() {}

    public int getMedicineId()              { return medicineId; }
    public void setMedicineId(int id)       { this.medicineId = id; }

    public String getName()                 { return name; }
    public void setName(String n)           { this.name = n; }

    public String getCategory()             { return category; }
    public void setCategory(String c)       { this.category = c; }

    public String getManufacturer()         { return manufacturer; }
    public void setManufacturer(String m)   { this.manufacturer = m; }

    public BigDecimal getUnitPrice()        { return unitPrice; }
    public void setUnitPrice(BigDecimal p)  { this.unitPrice = p; }

    public int getStockQty()                { return stockQty; }
    public void setStockQty(int q)          { this.stockQty = q; }

    public Date getExpiryDate()             { return expiryDate; }
    public void setExpiryDate(Date d)       { this.expiryDate = d; }

    public Timestamp getCreatedAt()         { return createdAt; }
    public void setCreatedAt(Timestamp t)   { this.createdAt = t; }
}
