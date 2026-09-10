# ज्ञानदीप वाचनालय — Library Admission & Access Portal

A Spring Boot web application for **Gyandeep Vachnalay** library featuring:

- 📝 **Online Admission Form** — members submit their details, exam category (MPSC / Police Bharti / etc.), and preferred shift.
- 🔐 **Role-based Login** — Spring Security with `ROLE_ADMIN` and `ROLE_USER`.
- 🛡️ **Admin Panel**
  - Dashboard with live stats (total/pending/approved/rejected admissions, total users)
  - Review admission requests → approve (assign seat number + membership validity) or reject
  - Manage every user account and grant fine-grained **library access permissions**:
    `Manage Admissions`, `Manage Books`, `Manage Users`, `View Reports`, plus enable/disable login
- 👤 **User Profile** — see your own access rights, admission/seat status, update contact details and password.
- 🎨 A custom navy-and-gold UI theme matching the library's branding, fully responsive.

## Tech Stack
Java 17 · Spring Boot 3.3 · Spring MVC · Spring Security · Spring Data JPA · Thymeleaf · H2 (in-memory, swappable) · Maven

## Project Structure
```
src/main/java/com/gyandeep/library/
  config/        SecurityConfig, DataInitializer (bootstraps default admin)
  model/         User, Admission, Role, AdmissionStatus
  repository/    UserRepository, AdmissionRepository
  service/       UserService, AdmissionService, CustomUserDetailsService
  controller/    HomeController, AuthController, AdmissionController,
                 ProfileController, AdminController
src/main/resources/
  templates/     Thymeleaf views (index, login, register, admission-form,
                  profile, admin/dashboard, admin/admissions, admin/users, ...)
  static/css/    style.css (navy/gold theme)
  application.properties
```

## Running Locally

1. Make sure you have **Java 17+** and **Maven** installed.
2. From the project root:
   ```bash
   mvn spring-boot:run
   ```
3. Open **http://localhost:8080**

### Default Admin Login
On first run, `DataInitializer` creates a default admin account:

| Field    | Value               |
|----------|---------------------|
| Username | `admin`             |
| Password | `Admin@123`          |

**Change this password immediately** after your first login (via My Profile → Change Password), or edit it beforehand in `application.properties` (`app.admin.default-*`).

### Regular Members
Anyone can self-register at `/register`, then log in and fill out the **Admission Form** (`/admission/new`). Their request appears in the Admin Panel under **Admissions & Seats** for approval.

## Database
By default the app uses an **in-memory H2 database** (data resets on restart) — perfect for demos. The H2 console is available at `/h2-console` (JDBC URL: `jdbc:h2:mem:gyandeeplib`).

### Switching to MySQL/PostgreSQL for production
Replace the H2 dependency in `pom.xml` with your driver (e.g. `mysql-connector-j`) and update `application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/gyandeep_library
spring.datasource.username=your_user
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

## Key URLs
| URL | Description |
|---|---|
| `/` | Public landing page |
| `/register`, `/login` | Auth pages |
| `/admission/new` | Admission form (logged-in users) |
| `/profile` | User profile, access rights, admission status |
| `/admin/dashboard` | Admin overview |
| `/admin/admissions` | Review & approve/reject admissions |
| `/admin/users` | Manage users & grant access permissions |

## Notes / Next Steps You Might Add
- Email/SMS notification on admission approval
- A real "Manage Books" catalog module (the access-right toggle is already wired in, ready for a `Book` entity + controller)
- Pagination on large admission/user lists
- Password-reset-by-email flow
