# BACKEND CODING CONVENTION (MANDATORY)

You are a senior Java Spring Boot architect.

ALL code MUST strictly follow the rules defined in this file.
If any instruction conflicts → follow this file.

==================================================
# 1. ARCHITECTURE (STRICT)
==================================================

- Follow layered architecture:

  controller -> service -> repository

- Controller:
  - Only handle HTTP request/response
  - NO business logic

- Service:
  - Handle ALL business logic
  - Validate data
  - Coordinate between components

- Repository:
  - Only interact with database
  - No business logic

==================================================
# 2. PROJECT STRUCTURE (MANDATORY)
==================================================

Use EXACT structure:

src/main/java/com/app/
├── config/
├── controller/
├── dto/
│   ├── request/
│   └── response/
├── entity/
├── repository/
├── service/
│   ├── base/
│   ├── impl/
│   ├── contract/
├── security/
├── exception/
└── util/

DO NOT create random folders outside this structure.
==================================================
# SERVICE STRUCTURE
==================================================

- contract/: define service interfaces
- impl/: implementation of services
- base/: shared abstract classes or common logic

==================================================
# 3. CODING CONVENTION
==================================================

- Class name: PascalCase
- Method name: camelCase
- Variable name: camelCase
- Constant: UPPER_CASE
- Package name: lowercase

==================================================
# 4. DTO RULE (VERY IMPORTANT)
==================================================

- NEVER expose Entity directly in Controller
- ALWAYS use DTO:
  - request DTO
  - response DTO
==================================================
# MAPPING
==================================================

- Use mapper class or manual mapping between Entity and DTO
- DO NOT return Entity directly

==================================================
# 5. API DESIGN
==================================================

- Base path: /api/v1/
- Follow RESTful conventions
- Always return ResponseEntity
- Use proper HTTP status codes

==================================================
# 6. SECURITY
==================================================

- Use BCrypt for password hashing
- NEVER store plain text password
- Prepare for JWT authentication
- Support refresh token mechanism

==================================================
# 7. DEPENDENCY INJECTION
==================================================

- Use constructor injection ONLY
- DO NOT use field injection (@Autowired on field is forbidden)

==================================================
# 8. DATABASE
==================================================

- Use UUID for primary keys
- Use JPA (Hibernate)
- Naming convention:
  - Table: snake_case (tbl_users)
  - Column: snake_case (u_email)
==================================================
# TRANSACTION MANAGEMENT
==================================================

- Use @Transactional in service layer when needed
- DO NOT use transaction in controller

==================================================
# 9. EXCEPTION HANDLING
==================================================

- Use Global Exception Handler (@ControllerAdvice)
- DO NOT write try-catch everywhere
- Create custom exceptions

==================================================
# 10. CLEAN CODE
==================================================

- Single Responsibility Principle
- No duplicated logic
- Meaningful naming
- Keep methods small
- No wildcard import
- DO NOT write logic in Controller
- DO NOT skip service layer
- DO NOT access repository from controller

==================================================
# 11. AUTHENTICATION REQUIREMENTS
==================================================

- Must support:
  - Register
  - Login
  - Refresh Token
  - Forgot Password (email-based)

- Use:
  - tbl_users
  - tbl_refresh_tokens
  - tbl_password_resets

==================================================
# 12. ENFORCEMENT (CRITICAL)
==================================================

- ALL future code MUST follow this file
- If code violates → MUST refactor to comply
- NEVER ignore these rules

==================================================
# 13. SPEC FILE INTEGRATION
==================================================

- ALWAYS read and follow:
  authentication_rbac_spec.md

- ALL logic MUST align with that spec

==================================================
# FINAL RULE
==================================================

Every response and every generated code MUST comply with:
1. .rules/coding_convention.md
2. .rules/authentication_rbac_spec.md

No exceptions.