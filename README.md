# PharmaCare 💊

A full-stack online pharmacy platform with medicine ordering, prescription management, Razorpay payments, AI chatbot, delivery tracking, and a loyalty rewards system.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Frontend | React 19, Vite, Tailwind CSS, React Router DOM, Axios |
| Backend | Spring Boot 3.2.3, Java 17, Spring Security, Spring AI |
| Database | MySQL 8.0 |
| Auth | JWT (jjwt 0.11.5) |
| Payment | Razorpay (Test Mode) |
| AI Chatbot | Groq API (llama-3.3-70b-versatile) |
| Notifications | Twilio SMS, Gmail SMTP |

---

## Project Structure

```
pharma/
├── backend/
│   ├── src/main/java/com/pharma/backend/
│   │   ├── config/          DataSeeder.java, WebMvcConfig.java
│   │   ├── controllers/     Auth, Medicine, Category, Order, Prescription,
│   │   │                    Coupon, Reward, Chatbot, Payment, Delivery
│   │   ├── entity/          User, Role, Medicine, Category, Order, OrderItem,
│   │   │                    Prescription, Coupon, Payment, DeliveryAssignment
│   │   ├── payload/         request/ + response/ DTOs
│   │   ├── repository/      JPA repositories for all entities
│   │   ├── security/        JWT, WebSecurityConfig, RateLimiting
│   │   └── service/         AiChatbotService, EmailService, SmsService
│   ├── src/main/resources/
│   │   ├── application.properties              ← safe to commit (no secrets)
│   │   ├── application-local.properties        ← YOUR secrets (gitignored)
│   │   ├── application-local.properties.example← template for new devs
│   │   └── data/medicines.csv
│   └── pom.xml
├── frontend/
│   ├── src/
│   │   ├── components/Chatbot/
│   │   ├── context/         AuthContext, CartContext, RewardContext
│   │   ├── pages/           Home, Login, Register, ForgotPassword,
│   │   │                    Dashboard, AdminDashboard, Offers,
│   │   │                    PaymentPage, DeliveryLogin, DeliveryDashboard
│   │   └── services/        api.js, authService.js
│   └── package.json
├── docker-compose.yml
└── .env
```

---

## Prerequisites

Before running the project, make sure you have:

- **Java 17+** — [Download](https://adoptium.net/)
- **Maven 3.8+** — [Download](https://maven.apache.org/download.cgi)
- **Node.js 18+** — [Download](https://nodejs.org/)
- **MySQL 8.0** — running locally on port `3306`
- **Docker** (optional, for Docker setup) — [Download](https://www.docker.com/)

---

## Quick Start

### Option 1 — Docker (Easiest)

Starts MySQL, backend, and frontend all at once. No manual setup needed.

```bash
# From the pharma/pharma directory
docker-compose up -d --build
```

Wait a few minutes for the build to complete, then open:

| Service | URL |
|---|---|
| Frontend | http://localhost:5173 |
| Backend API | http://localhost:8081 |

To stop:
```bash
docker-compose down
```

---

### Option 2 — Manual (For Development)

#### Step 1 — Set up the database

Make sure MySQL is running on port `3306`. The backend will auto-create the `pharma_db` database on first start.

If you get a roles column error on an existing database, run this once:
```bash
mysql -u root -p pharma_db -e "ALTER TABLE roles MODIFY COLUMN name VARCHAR(30);"
```

#### Step 2 — Configure secrets

Copy the example secrets file and fill in your values:
```bash
# From pharma/pharma/backend/src/main/resources/
cp application-local.properties.example application-local.properties
```

Then open `application-local.properties` and set your real values (see the [Secrets Setup](#secrets-setup) section below).

#### Step 3 — Start the backend

```bash
cd pharma/pharma/backend
mvn spring-boot:run
```

Backend starts on **http://localhost:8081**

#### Step 4 — Start the frontend

Open a second terminal:
```bash
cd pharma/pharma/frontend
npm install       # first time only
npm run dev
```

Frontend starts on **http://localhost:5173**

---

## Secrets Setup

All secrets live in `application-local.properties` which is **gitignored and never committed**.

### 1. Copy the template
```bash
cp backend/src/main/resources/application-local.properties.example \
   backend/src/main/resources/application-local.properties
```

### 2. Fill in your values

```properties
# Database
spring.datasource.username=root
spring.datasource.password=your_mysql_password

# JWT Secret — use any long random string
jwt.secret=your_64_char_base64_secret

# Email (Gmail SMTP)
# Use a Gmail App Password, not your real password
# Google Account → Security → 2-Step Verification → App Passwords
spring.mail.username=your_email@gmail.com
spring.mail.password=your_gmail_app_password

# Razorpay (get from https://dashboard.razorpay.com)
razorpay.key.id=rzp_test_xxxxxxxxxxxx
razorpay.key.secret=your_razorpay_secret

# Groq AI (get from https://console.groq.com)
groq.api-key=your_groq_api_key

# Google Gemini AI (get from https://aistudio.google.com)
spring.ai.google.genai.api-key=your_gemini_api_key

# Twilio SMS (get from https://console.twilio.com)
twilio.account.sid=your_twilio_sid
twilio.auth.token=your_twilio_token
twilio.phone.number=+1234567890
```

> **How it works:** `application.properties` has `spring.profiles.active=local`, which tells Spring Boot to automatically load `application-local.properties` and use its values to override the placeholders.

---

## Default Login Credentials

These accounts are seeded automatically on first startup.

| Role | Username | Password | Login URL |
|---|---|---|---|
| Admin | `admin` | `Ramireddy@2004` | http://localhost:5173/admin |
| Delivery Agent | `delivery1` | `Delivery@123` | http://localhost:5173/delivery/login |
| User | Register via UI | — | http://localhost:5173/login |

---

## Medicine Images

Uploaded medicine images are stored at:
```
backend/src/main/resources/static/uploads/medicines/
```

This folder is included in the zip so images work on any machine without re-uploading.

If images are not showing after unzipping:
```bash
# Windows — copy from old location if needed
xcopy /E /I "backend\uploads" "backend\src\main\resources\static\uploads"

# Then restart the backend
cd pharma/pharma/backend
mvn spring-boot:run
```

Images are served at: `http://localhost:8081/uploads/medicines/<filename>`

---

## Order Status Flow

```
ORDER_CREATED → PENDING_PAYMENT → PAID → INITIATED → DISPATCHED → DELIVERED
                                                ↓
                                         CANCELLED / FAILED
```

| Status | Triggered By |
|---|---|
| `ORDER_CREATED` | User places order |
| `PENDING_PAYMENT` | Razorpay order created |
| `PAID` | Payment verified successfully |
| `INITIATED` | Admin approves the order |
| `DISPATCHED` | Delivery agent marks Out for Delivery |
| `DELIVERED` | Delivery agent marks Delivered |
| `CANCELLED` | Admin or user cancels |
| `FAILED` | Payment failed |

---

## Delivery Flow

```
Admin sees PAID order → Approves (INITIATED) → Assigns delivery agent
Delivery agent logs in → Accepts order → Marks Out for Delivery → Marks Delivered
User sees live 5-step timeline in /dashboard
```

---

## Razorpay Test Payment Details

Use these details to simulate a successful payment in test mode:

| Field | Value |
|---|---|
| Card Number | `4718 6092 0990 8132` |
| Expiry | `12/26` |
| CVV | `123` |
| OTP | `1234` |
| Net Banking | Select any bank → click "Success" |

---

## Default Seeded Coupons

| Code | Type | Value | Min Order |
|---|---|---|---|
| `WELCOME10` | Percentage | 10% off | None |
| `FLAT100` | Flat | ₹100 off | ₹500 |
| `WELLNESS20` | Percentage | 20% off | ₹1000 |

---

## API Reference

Base URL: `http://localhost:8081`

Protected endpoints require the header:
```
Authorization: Bearer <your_jwt_token>
```

### Authentication
| Method | Endpoint | Auth |
|---|---|---|
| POST | `/api/auth/signup` | Public |
| POST | `/api/auth/signin` | Public |
| POST | `/api/auth/forgot-password` | Public |
| POST | `/api/auth/verify-otp` | Public |
| POST | `/api/auth/reset-password` | Public |

### Medicines & Categories
| Method | Endpoint | Auth |
|---|---|---|
| GET | `/api/medicines` | Public |
| GET | `/api/medicines/{id}` | Public |
| GET | `/api/medicines/search?query=` | Public |
| GET | `/api/medicines/category/{id}` | Public |
| POST | `/api/medicines` | Admin |
| PUT | `/api/medicines/{id}` | Admin |
| DELETE | `/api/medicines/{id}` | Admin |
| POST | `/api/medicines/{id}/image` | Admin |
| GET | `/api/categories` | Public |
| POST | `/api/categories` | Admin |
| DELETE | `/api/categories/{id}` | Admin |

### Orders
| Method | Endpoint | Auth |
|---|---|---|
| POST | `/api/orders` | User |
| GET | `/api/orders/user` | User |
| DELETE | `/api/orders/{id}` | User (cancel) |
| POST | `/api/orders/{id}/reorder` | User |
| GET | `/api/orders` | Admin |
| PUT | `/api/orders/{id}/status` | Admin |

### Prescriptions
| Method | Endpoint | Auth |
|---|---|---|
| POST | `/api/prescriptions/upload/{orderId}` | User |
| PUT | `/api/prescriptions/{id}/validate` | Admin |

### Payment
| Method | Endpoint | Auth |
|---|---|---|
| POST | `/api/payment/create-order` | User |
| POST | `/api/payment/verify` | User |
| POST | `/api/payment/failure` | User |
| GET | `/api/payment/order/{orderId}` | User / Admin |

### Coupons & Rewards
| Method | Endpoint | Auth |
|---|---|---|
| GET | `/api/coupons` | Public |
| POST | `/api/coupons/validate` | Public |
| POST | `/api/coupons` | Admin |
| PUT | `/api/coupons/{id}` | Admin |
| DELETE | `/api/coupons/{id}` | Admin |
| GET | `/api/rewards/status` | User |
| POST | `/api/rewards/claim-login` | User |

### Delivery
| Method | Endpoint | Auth |
|---|---|---|
| POST | `/api/delivery/assign` | Admin |
| GET | `/api/delivery/agents` | Admin |
| GET | `/api/delivery/assignments` | Admin |
| GET | `/api/delivery/orders` | Delivery Agent |
| PUT | `/api/delivery/{id}/accept` | Delivery Agent |
| PUT | `/api/delivery/{id}/status` | Delivery Agent |
| GET | `/api/delivery/{id}` | Agent / Admin |

### Chatbot
| Method | Endpoint | Auth |
|---|---|---|
| POST | `/api/chat` | Public |

Swagger UI is available at: http://localhost:8081/swagger-ui.html

---

## Deploying to Production

When deployed, anyone who visits your link and registers will have their data stored in your cloud database, visible in your admin panel at `/admin`.

You need to deploy 3 things: a cloud database, the backend, and the frontend.

---

### Step 1 — Cloud MySQL Database

**Recommended: [Railway](https://railway.app)** (free tier)

1. Sign up at railway.app
2. New Project → Add a service → Database → MySQL
3. Click the MySQL service → go to the **Connect** tab
4. Note down: `MYSQL_HOST`, `MYSQL_PORT`, `MYSQL_USER`, `MYSQL_PASSWORD`, `MYSQL_DATABASE`

Other free options: [PlanetScale](https://planetscale.com), [Aiven](https://aiven.io)

---

### Step 2 — Deploy the Backend

**Recommended: [Render](https://render.com)** (free tier)

1. Push your project to GitHub
2. Go to render.com → New → Web Service → connect your GitHub repo
3. Set the following:
   - **Build Command:** `cd pharma/pharma/backend && mvn clean package -DskipTests`
   - **Start Command:** `java -jar pharma/pharma/backend/target/backend-0.0.1-SNAPSHOT.jar`
4. Under **Environment Variables**, add all your secrets:

| Key | Value |
|---|---|
| `spring.datasource.url` | `jdbc:mysql://YOUR_DB_HOST:PORT/DATABASE?useSSL=true&allowPublicKeyRetrieval=true` |
| `spring.datasource.username` | your cloud DB username |
| `spring.datasource.password` | your cloud DB password |
| `jwt.secret` | your JWT secret |
| `spring.mail.username` | your Gmail address |
| `spring.mail.password` | your Gmail App Password |
| `razorpay.key.id` | your Razorpay key ID |
| `razorpay.key.secret` | your Razorpay secret |
| `groq.api-key` | your Groq API key |
| `spring.ai.google.genai.api-key` | your Gemini API key |
| `app.cors.allowed-origins` | your Vercel frontend URL (set after Step 3) |

5. Deploy — Render gives you a URL like `https://pharmacare-backend.onrender.com`

---

### Step 3 — Deploy the Frontend

**Recommended: [Vercel](https://vercel.com)** (free, best for React/Vite)

1. Go to vercel.com → New Project → import your GitHub repo
2. Set the **Root Directory** to `pharma/pharma/frontend`
3. Under **Environment Variables**, add:

| Key | Value |
|---|---|
| `VITE_API_URL` | `https://pharmacare-backend.onrender.com` (your Render URL from Step 2) |

4. Deploy — Vercel gives you a URL like `https://pharmacare.vercel.app`

---

### Step 4 — Connect Frontend URL to Backend CORS

Go back to your Render backend → Environment Variables → update:

```
app.cors.allowed-origins = https://pharmacare.vercel.app
```

Redeploy the backend. Now:
- Anyone visits `https://pharmacare.vercel.app`
- They register → data goes to your cloud MySQL
- You log in as admin at `https://pharmacare.vercel.app/admin`
- You see all users, orders, prescriptions in your admin panel

---

### Build commands (manual JAR deploy)

```bash
# Backend JAR
cd pharma/pharma/backend
mvn clean package -DskipTests
java -jar target/backend-0.0.1-SNAPSHOT.jar

# Frontend static build
cd pharma/pharma/frontend
VITE_API_URL=https://your-backend.onrender.com npm run build
# Upload the dist/ folder to any static host
```

---

## Security Notes

- **Role assignment** — users can only register as `ROLE_USER`. Admin and delivery agent roles must be assigned directly in the database. They cannot be self-assigned via the API.
- **Secrets** — `application-local.properties` is gitignored. Never commit real credentials.
- **JWT** — tokens expire after 24 hours (`jwt.expiration=86400000`).
- **Passwords** — stored as BCrypt hashes, never in plain text.
- **Payment** — Razorpay signatures are verified server-side using HMAC-SHA256 before any order is marked as paid.

---

## Useful Commands

```bash
# Start backend
cd pharma/pharma/backend
mvn spring-boot:run

# Full clean rebuild
mvn clean spring-boot:run

# Start frontend
cd pharma/pharma/frontend
npm run dev

# Build backend JAR for deployment
mvn clean package -DskipTests

# MySQL shell
mysql -u root -p pharma_db

# Fix roles column (run once on existing DB if you get a column error)
mysql -u root -p pharma_db -e "ALTER TABLE roles MODIFY COLUMN name VARCHAR(30);"

# Docker — start everything
docker-compose up -d --build

# Docker — stop everything
docker-compose down
```
