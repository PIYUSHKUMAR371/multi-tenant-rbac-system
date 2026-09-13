# Multi-Tenant Enterprise Role-Based Access Control (RBAC) System

**Author** - Piyush Kumar  
**Repository** - [github.com/PIYUSHKUMAR371/multi-tenant-rbac-system](https://github.com/PIYUSHKUMAR371/multi-tenant-rbac-system)

## 📌 Project Overview
This project is an enterprise-grade backend REST API built with **Java and Spring Boot 3**. It implements a highly secure, **Multi-Tenant Role-Based Access Control (RBAC)** architecture. 

In modern enterprise applications (like SaaS products or internal corporate tools), multiple organizations (Tenants) share the same underlying infrastructure. This system ensures strict **logical data isolation** between tenants while providing dynamic, granular access control through JSON Web Tokens (JWT) and a complex relational database schema.

This project was built to demonstrate core backend engineering principles, including secure API design, stateless authentication, database relationship mapping, and centralized exception handling.

## 🚀 Key Features

*   **Multi-Tenancy Data Isolation:** Users, Roles, and operations are strictly scoped to their respective `tenantCode`. Users in `TENANT_A` cannot interact with or access data from `TENANT_B`.
*   **Dynamic Role-Based Access Control:** Instead of hardcoding roles, the system supports dynamic role creation. Administrators can group granular system permissions (e.g., `USER_READ`, `ROLE_WRITE`) into custom roles and assign them to users.
*   **Stateless Authentication (JWT):** Secured with Spring Security 6 and JSON Web Tokens. Passwords are cryptographically hashed using BCrypt.
*   **Enterprise Exception Handling:** Utilizes `@RestControllerAdvice` to intercept errors and return standardized, consumer-friendly JSON HTTP responses instead of raw server stack traces.
*   **Clean Layered Architecture:** Strict separation of concerns using Controllers, Services, Repositories, Entities, and DTOs (Data Transfer Objects).

## 🛠️ Tech Stack

*   **Language:** Java 17 / 21
*   **Framework:** Spring Boot 3.x
*   **Security:** Spring Security, JWT (io.jsonwebtoken), BCrypt
*   **Database Integration:** Spring Data JPA / Hibernate
*   **Database:** H2 (In-memory for local dev) / PostgreSQL (Production ready)
*   **Build Tool:** Maven
*   **Boilerplate Reduction:** Lombok

## 🗄️ Relational Database Architecture

The system uses a highly normalized relational database design to handle complex Many-to-Many mappings:

1.  **Tenants:** Top-level organizations (`id`, `tenantCode`, `organizationName`).
2.  **Users:** Belong strictly to one Tenant.
3.  **Roles:** Scoped to a specific Tenant (e.g., "Google India Admin").
4.  **Permissions:** Universal granular actions defined at startup (e.g., `USER_DELETE`).
5.  **Join Tables:** `user_roles` and `role_permissions` handle the mapping logic.

## ⚙️ API Endpoints Reference

### Authentication (Public)
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/auth/register` | Registers a new user & dynamically provisions a tenant if needed. |
| `POST` | `/api/auth/login` | Authenticates credentials and returns a signed JWT Bearer Token. |

### Role Management (Requires JWT)
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/roles/create` | Creates a custom role with specific permissions for a tenant. |
| `POST` | `/api/roles/assign` | Assigns an existing role to a specific user. |
| `GET` | `/api/roles/tenant/{tenantCode}`| Fetches all roles registered under a specific tenant. |

### User Management (Requires JWT)
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/api/users/profile/{username}` | Returns user profile and aggregated permissions across all roles. |
| `GET` | `/api/users/tenant/{tenantCode}`| Lists all users within a specific tenant space. |
| `PATCH`| `/api/users/{id}/status` | Enables or disables a user account (suspension). |

### Tenant Administration (Requires JWT)
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/api/tenants` | Lists all registered tenants in the system. |
| `GET` | `/api/tenants/{tenantCode}` | Retrieves details for a specific tenant organization. |

## 💻 How to Run Locally

### Prerequisites
*   JDK 17 or higher
*   Maven

### Instructions
1.  **Clone the repository:**
    ```bash
    git clone [https://github.com/PIYUSHKUMAR371/multi-tenant-rbac-system.git](https://github.com/PIYUSHKUMAR371/multi-tenant-rbac-system.git)
    cd multi-tenant-rbac-system
    ```
2.  **Run the application:**
    ```bash
    ./mvnw spring-boot:run
    ```
3.  **Access the in-memory database console:**
    *   Navigate to `http://localhost:8080/h2-console`
    *   JDBC URL: `jdbc:h2:mem:rbacdb`
    *   Username: `sa`
    *   Password: `password`

### Testing the Application
1.  Use a tool like **Postman** or **Thunder Client** (VS Code).
2.  Send a POST request to `/api/auth/register` to create a user.
3.  Send a POST request to `/api/auth/login` to get your JWT.
4.  Copy the `token` from the response.
5.  For all subsequent API calls (Roles, Users, Tenants), add a Header: 
    `Authorization: Bearer <your_copied_token>`.
