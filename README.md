# hospital-management-system
A full-stack Hospital Management System built with Java Servlet, JDBC, MySQL, HTML, CSS and JavaScript. Manages Patients, Doctors, Appointments, Billing, Pharmacy, Rooms, Staff and Lab Reports.

java servlet jdbc mysql html css javascript apache-tomcat hospital-management crud-operations full-stack

# 🏥 Hospital Management System

A full-stack **Hospital Management System** built using **Java Servlet + JDBC + MySQL** 
for the backend and **HTML + CSS + JavaScript** for the frontend.

---

## 🚀 Live Features

- 📊 **Dashboard** — Real-time stats for patients, doctors, appointments
- 👤 **Patients** — Add, edit, delete, search patient records
- 👨‍⚕️ **Doctors** — Manage doctor profiles and availability
- 📅 **Appointments** — Book and track patient appointments
- 🛏️ **Rooms** — Room availability and occupancy management
- 💊 **Pharmacy** — Medicine inventory with stock alerts
- 💳 **Billing** — Itemized bills with payment tracking
- 👥 **Staff** — Hospital staff management
- 🔬 **Lab Reports** — Diagnostic test management

---

## 🛠️ Tech Stack

| Layer      | Technology                        |
|------------|-----------------------------------|
| Frontend   | HTML5, CSS3, Vanilla JavaScript   |
| Backend    | Java 17, Jakarta Servlet (EE 5.0) |
| Database   | MySQL 8.x via JDBC                |
| Server     | Apache Tomcat 10.1                |
| JSON       | Google Gson 2.10.1                |
| IDE        | Eclipse IDE for Enterprise Java   |

---

## 📁 Project Structure

hospital-management-system/
├── src/main/java/com/hospital/
│   ├── model/          # Patient, Doctor, Room, Bill...
│   ├── dao/            # JDBC database operations
│   ├── servlet/        # REST API endpoints
│   └── util/           # DBConnection, CORSFilter
├── src/main/webapp/
│   ├── index.html      # Main frontend UI
│   ├── css/style.css   # Stylesheet
│   ├── js/app.js       # Frontend JavaScript
│   └── WEB-INF/
│       ├── web.xml     # Servlet mappings
│       └── lib/        # gson + mysql connector JARs
└── sql/
└── hospital_schema.sql  # Database schema + seed data

---

## ⚙️ Setup Instructions

### 1. Clone the repository
```bash
git clone https://github.com/yourusername/hospital-management-system.git
```

### 2. Setup MySQL Database
```bash
mysql -u root -p < sql/hospital_schema.sql
```

### 3. Update DB Password
Open `src/main/java/com/hospital/util/DBConnection.java`:
```java
private static final String PASSWORD = "your_mysql_password";
```

### 4. Add JAR files to WEB-INF/lib
- [mysql-connector-j-9.7.0.jar](https://dev.mysql.com/downloads/connector/j/)
- [gson-2.10.1.jar](https://repo1.maven.org/maven2/com/google/code/gson/gson/2.10.1/)

### 5. Deploy to Tomcat
- Import project into Eclipse as **Dynamic Web Project**
- Add **Apache Tomcat 10.1** as server
- Right-click project → **Run As → Run on Server**

### 6. Open in browser
http://localhost:8080/hospital/

---

## 📸 Screenshots

> Dashboard, Patients, Doctors, Appointments, Billing modules

**

---

## 🗄️ Database Schema

| Table         | Description                    |
|---------------|-------------------------------|
| patients      | Patient records                |
| doctors       | Doctor profiles                |
| appointments  | Appointment scheduling         |
| rooms         | Room management                |
| medicines     | Pharmacy inventory             |
| bills         | Billing and payments           |
| staff         | Hospital staff records         |
| lab_reports   | Diagnostic test reports        |

---
