AUTHENTICATION & RBAC READY SPEC
Stack: Spring Boot + Spring Security + JWT
Password: BCrypt
Purpose: Implement Authentication correctly from the beginning and be RBAC-ready for future expansion.
1. DATABASE RULES
•	tbl_users:
- u_email must be UNIQUE.
- u_password must store BCrypt hash only.
- Use soft delete via u_deleted_at.
- u_email_verified must be checked before login success.
•	tbl_refresh_tokens:
- One login = one refresh token record.
- Must validate expiration and revoked status.
•	tbl_password_resets:
- Used for VERIFY_EMAIL and RESET_PASSWORD.
- Token must be one-time use.
- Must validate expiration and type.
2. REGISTER FLOW
•	- Validate email format and uniqueness.
- Validate password >= 8 characters.
- Hash password using BCrypt.
- Set u_email_verified = false.
- Auto assign default role: USER (RBAC future-ready).
- Generate VERIFY_EMAIL token (24h).
3. LOGIN FLOW
•	- Check user exists.
- Check u_deleted_at IS NULL.
- Check u_email_verified = true.
- Check u_lock_until is null or expired.
- Compare password with BCrypt.
- On success: update u_last_login_at.
- Generate Access Token (15 min).
- Generate Refresh Token (7 days).
4. JWT RULES (RBAC READY)
•	- Access Token must contain:
  userId
  email
  roles (e.g., ["USER"])
- Do NOT store password in token.
5. REFRESH FLOW
•	- Validate refresh token exists.
- Must not be revoked.
- Must not be expired.
- Issue new Access Token.
6. RESET PASSWORD FLOW
•	- Generate RESET_PASSWORD token (15 min).
- Validate token type and expiration.
- Hash new password using BCrypt.
- Mark token as used.
7. RBAC FUTURE RULES (Do NOT implement fully yet)
•	- Always load user roles at login.
- Include roles inside JWT claim.
- Future protected endpoints will use @PreAuthorize with roles.
- Default role on register = USER.
- Authentication must be independent from authorization logic.
8. SECURITY REQUIREMENTS
•	- Never store raw password.
- Never return password in API response.
- Always validate token expiration.
- Always check soft delete.
