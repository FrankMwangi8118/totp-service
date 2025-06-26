# 🔐 Spring Boot TOTP Authentication

A Spring Boot application that implements **Two-Factor Authentication (2FA)** using **Time-based One-Time Passwords (TOTP)**, compatible with Google Authenticator and similar apps.


## 🚀 Getting Started

### Prerequisites

- Java 17+
- Maven
- Authenticator app

### Run the App

```bash
git clone https://github.com/your-username/your-project.git
cd your-project
./mvnw spring-boot:run
---
### 🛠️ How It Works

1. **User Registration**
    - The user registers using a username (no password for now).
    - A TOTP **secret key** is generated and returned in the response.
    - A **QR code image** is created and saved in the root (`./`) of the project directory.
    - The user scans the QR code using an authenticator app.

2. **Code Verification**
    - The authenticator app generates a 6-digit TOTP code every 30 seconds.
    - The user sends this code, along with their access token, to verify their identity.

---
### 🔄 Flow Summary

#### 1.Register User

**Request:**
```json
POST http://localhost:8080/api/v1/auth/register

{
  "username": "johndoe"
}
```
**Response:**
```json

 {
  "secret": "JBSWY3DPEHPK3PXP",
  "qrImagePath": "./qrcode_johndoe.png"
}
```
#### 2. verify totp
```json
POST http://localhost:8080/api/v1/auth/login

{
"totpCode": ,
"secretKey":"secret_here"
}

```

##  Want to Collaborate?

I’m actively improving this project and open to contributors!

Whether you:
- Have suggestions 💡
- Want to help build a frontend 🖥️
- Can enhance security 🔐
- Or just want to explore TOTP with Spring Boot...

Feel free to **fork the repo**, open issues, or submit a **pull request**.

---

### 🛠 Contributing Guidelines

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/your-feature`)
3. Commit your changes (`git commit -m 'Add something'`)
4. Push to the branch (`git push origin feature/your-feature`)
5. Open a Pull Request 🚀

---

##  Contact

You can reach out via:
- GitHub Issues
- https://www.linkedin.com/in/frank-mwangi-dev

Let’s build something secure together! 🔐🔥
