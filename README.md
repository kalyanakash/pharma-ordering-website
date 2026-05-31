# 💊 PharmaCare — Online Pharmacy Management System

A full-stack pharmacy ordering platform built with **React + Spring Boot + MySQL**. Features medicine catalog, prescription management, AI chatbot, loyalty rewards, coupon system, and a complete admin dashboard.

---

## 🚀 Tech Stack

| Layer | Technology |
|---|---|
| Frontend | React 19, Vite, Tailwind CSS, React Router DOM, Axios |
| Backend | Spring Boot 3.2.3, Java 17, Spring Security |
| Database | MySQL 8.0 |
| Auth | JWT (jjwt 0.11.5) |
| AI Chatbot | Groq API (llama-3.3-70b-versatile) |
| Notifications | Gmail SMTP, Twilio SMS |

---

## ✨ Features

- 🔐 JWT Authentication — Login, Register, Forgot Password (OTP via email)
- 💊 Medicine Catalog — Browse, search, filter by category with images
- 🛒 Shopping Cart — Add/remove medicines, quantity controls
- 📦 Order Management — Place orders, track status, cancel with refund
- 📋 Prescription Upload — Upload Rx images, admin validation
- 🎟️ Coupon System — FLAT & PERCENTAGE discounts with expiry
- ⭐ Loyalty Points — Earn & redeem points (10 pts = ₹1)
- 🤖 AI Health Assistant — Powered by Groq (llama-3.3-70b)
- 📧 Email Notifications — Order confirmation & status updates
- 👨‍💼 Admin Dashboard — Orders, Medicines, Categories, Coupons management

---

## 🖥️ Project Screenshots

### 🏠 Home Page
![Home Page](screenshots/home.png)

### 🔐 Login Page
![Login Page](screenshots/login.png)

### 💊 Medicine Catalog
![Medicine Catalog](screenshots/catalog.png)

### 🛒 Shopping Cart
![Shopping Cart](screenshots/cart.png)

### 📊 User Dashboard
![User Dashboard](screenshots/dashboard.png)

### 🎟️ Offers & Coupons
![Offers Page](screenshots/offers.png)

### 🤖 AI Health Chatbot
![AI Chatbot](screenshots/chatbot.png)

### 👨‍💼 Admin Dashboard — Orders
![Admin Orders](screenshots/admin-orders.png)

### 💊 Admin Dashboard — Medicines
![Admin Medicines](screenshots/admin-medicines.png)

### 🎟️ Admin Dashboard — Coupons
![Admin Coupons](screenshots/admin-coupons.png)

### 🔑 Forgot Password (OTP Flow)
![Forgot Password](screenshots/forgot-password.png)

> 📸 **To add screenshots:** Create a `screenshots/` folder inside `pharma/pharma/` and add your images with the names above.

---

## ⚙️ Prerequisites

Make sure you have these installed:

- **Java 17+** — [Download](https://adoptium.net)
- **Maven 3.9+** — [Download](https://maven.apache.org)
- **Node.js 18+** — [Download](https://nodejs.org)
- **MySQL 8.0** — [Download](https://dev.mysql.com/downloads)

---

## 🏃 Running the Project

### Step 1 — Start MySQL

Make sure MySQL is running on your machine (port 3306).

Create the database:
```sql
CREATE DATABASE IF NOT EXISTS pharma_db;
```

### Step 2 — Run Backend

Open **Terminal 1**:

```bash
cd "d:\pharma (3)\pharma\pharma\backend"
mvn spring-boot:run
```

Backend starts at: **http://localhost:8081**

### Step 3 — Run Frontend

Open **Terminal 2**:

```bash
cd "d:\pharma (3)\pharma\pharma\frontend"

# First time only
npm install

# Start dev server
npm run dev
```

Frontend starts at: **http://localhost:5173**

---

## 🔑 Default Credentials

| Role | Username | Password |
|---|---|---|
| Admin | `admin` | `Ramireddy@2004` |
| User | Register via UI | — |

---

## 📁 Project Structure

```
pharma/pharma/
├── backend/                    # Spring Boot API
│   ├── src/main/java/com/pharma/backend/
│   │   ├── config/             # DataSeeder, WebMvcConfig
│   │   ├── controllers/        # REST API endpoints
│   │   ├── entity/             # Database models
│   │   ├── payload/            # Request/Response DTOs
│   │   ├── repository/         # JPA repositories
│   │   ├── security/           # JWT, Spring Security
│   │   ├── seeder/             # CSV medicine importer
│   │   └── service/            # Business logic
│   └── src/main/resources/
│       ├── application.properties
│       └── data/medicines.csv  # 72 medicines auto-imported
│
├── frontend/                   # React SPA
│   └── src/
│       ├── components/Chatbot/ # AI chat widget
│       ├── context/            # Auth, Cart, Reward state
│       ├── pages/              # All page components
│       └── services/           # API calls
│
├── Project_Documentation.md    # Full technical docs
├── SQL_Documentation.md        # All SQL queries
└── README.md                   # This file
```

---

## 🌐 API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/auth/signup` | Register |
| POST | `/api/auth/signin` | Login |
| POST | `/api/auth/forgot-password` | Send OTP |
| POST | `/api/auth/verify-otp` | Verify OTP |
| POST | `/api/auth/reset-password` | Reset password |
| GET | `/api/medicines` | Get all medicines |
| POST | `/api/orders` | Place order |
| GET | `/api/orders/user` | My orders |
| DELETE | `/api/orders/{id}` | Cancel order |
| POST | `/api/chat` | AI chatbot |
| GET | `/api/coupons` | Get coupons |
| GET | `/api/rewards/status` | Loyalty points |

Full API docs: **http://localhost:8081/swagger-ui.html**

---

## 🗄️ Database

```sql
-- Connect
mysql -u root -pKalyan@234 pharma_db

-- View all tables
SHOW TABLES;

-- View medicines
SELECT m.name, m.price, c.name AS category
FROM medicines m
JOIN categories c ON m.category_id = c.id;

-- View orders
SELECT o.id, u.username, o.status, o.total_amount
FROM orders o JOIN users u ON o.user_id = u.id;
```

---

## 🔧 Environment Variables

| Variable | Purpose |
|---|---|
| `spring.datasource.password` | MySQL password |
| `groq.api-key` | Groq AI chatbot key |
| `spring.mail.username` | Gmail for OTP emails |
| `spring.mail.password` | Gmail App Password |
| `jwt.secret` | JWT signing secret |

---

## 👥 Team

Built by a team of 4 members:

| Member | Feature Area |
|---|---|
| Member 1 | Authentication, Security, Forgot Password (OTP) |
| Member 2 | Medicine Catalog, Images, AI Chatbot |
| Member 3 | Orders, Prescriptions, Email/SMS Notifications |
| Member 4 | Coupons, Loyalty Rewards, Admin Dashboard |

---

## 📄 License

This project is built for educational purposes.
