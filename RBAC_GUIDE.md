## RBAC cơ bản cho SI-Backend

Tài liệu này mô tả cách **seed dữ liệu RBAC** và **sử dụng quyền** trong code, dựa trên schema hiện tại.

> Lưu ý: trong DB, trường `tbl_permissions.name` đóng vai trò **permission code**, được map trực tiếp sang `GrantedAuthority`.

---

### 1. Bảng và cột liên quan

- **`tbl_users`**
  - `id` (UNIQUEIDENTIFIER)
  - `email`
  - `password`
  - `status` (`ACTIVE`, `INACTIVE`, …)
- **`tbl_roles`**
  - `id`
  - `name`
- **`tbl_permissions`**
  - `id`
  - `name` → **permission code**, ví dụ: `USER_READ`, `USER_WRITE`
  - `description`
- **`tbl_user_roles`**
  - `user_id`
  - `role_id`
- **`tbl_role_permissions`**
  - `role_id`
  - `permission_id`

---

### 2. Seed quyền (permissions)

```sql
-- Permissions (tbl_permissions)
INSERT INTO tbl_permissions (id, name, description)
VALUES
  (NEWID(), 'USER_READ',  N'Read user'),
  (NEWID(), 'USER_WRITE', N'Write user');
```

> Có thể thêm các permission khác như `ROLE_MANAGE`, `MENU_READ`, … theo nhu cầu.  
> Trường `name` chính là **authority string** dùng trong `@PreAuthorize`.

---

### 3. Seed role

```sql
-- Roles (tbl_roles)
INSERT INTO tbl_roles (id, name, description)
VALUES
  (NEWID(), 'USER',  N'Normal user'),
  (NEWID(), 'ADMIN', N'Administrator');
```

> Ghi lại `id` của các role này (USER, ADMIN) để dùng ở bước gán permission và user-role.

---

### 4. Gán permission cho role

Ví dụ:
- Role `USER` có quyền `USER_READ`
- Role `ADMIN` có quyền `USER_READ`, `USER_WRITE`

Giả sử:
- `@userRoleId`  = id của role `USER`
- `@adminRoleId` = id của role `ADMIN`
- `@permUserReadId`  = id của permission `USER_READ`
- `@permUserWriteId` = id của permission `USER_WRITE`

```sql
-- USER → USER_READ
INSERT INTO tbl_role_permissions (role_id, permission_id)
VALUES (@userRoleId, @permUserReadId);

-- ADMIN → USER_READ, USER_WRITE
INSERT INTO tbl_role_permissions (role_id, permission_id)
VALUES
  (@adminRoleId, @permUserReadId),
  (@adminRoleId, @permUserWriteId);
```

---

### 5. Gán role cho user

Giả sử đã có user trong `tbl_users`:

```sql
-- Tạo user demo (password đã hash bằng BCrypt trong ứng dụng)
INSERT INTO tbl_users (id, email, password, full_name, status, email_verified, created_at, updated_at)
VALUES (
  NEWID(),
  'admin@example.com',
  '<BCrypt hash ở đây>',
  N'Admin',
  'ACTIVE',
  1,
  SYSUTCDATETIME(),
  SYSUTCDATETIME()
);
```

Ghi lại:
- `@adminUserId` = id của user `admin@example.com`

Gán role:

```sql
-- admin user → ADMIN role
INSERT INTO tbl_user_roles (user_id, role_id)
VALUES (@adminUserId, @adminRoleId);
```

---

### 6. Cách mapping authority trong code

Trong `UserDetailsServiceImpl`:

- Hệ thống load `User` từ DB, sau đó duyệt:
  - `user.userRoles` → `Role`
  - `role.rolePermissions` → `Permission`
- Với mỗi `Permission`, lấy `permission.getName()` và map thành:

```java
new SimpleGrantedAuthority(permission.getName());
```

→ Nghĩa là nếu trong DB `tbl_permissions.name = 'USER_READ'` thì  
`Authentication` hiện tại sẽ có authority `"USER_READ"`.

---

### 7. Sử dụng trong code với `@PreAuthorize`

Ví dụ bảo vệ một API chỉ cho phép user có quyền đọc user:

```java
import org.springframework.security.access.prepost.PreAuthorize;

@PreAuthorize("hasAuthority('USER_READ')")
public List<UserDto> getUsers() {
    // ...
}
```

Hoặc cho phép cả READ và WRITE:

```java
@PreAuthorize("hasAnyAuthority('USER_READ', 'USER_WRITE')")
public UserDto updateUser(...) {
    // ...
}
```

> `@EnableMethodSecurity` đã được bật trong `SecurityConfig`, nên có thể dùng `@PreAuthorize` trên controller hoặc service.

---

### 8. Quy ước đặt tên permission (gợi ý)

Để hệ thống dễ scale và dễ đọc:

- Dùng pattern: `RESOURCE_ACTION` (VIẾT HOA, snake_case)
  - Ví dụ:
    - `USER_READ`, `USER_CREATE`, `USER_UPDATE`, `USER_DELETE`
    - `ROLE_MANAGE`
    - `MENU_READ`
    - `DOCUMENT_APPROVE`
- Tránh dùng dấu cách hoặc ký tự đặc biệt trong `name`.

---

### 9. Tóm tắt luồng RBAC cơ bản

1. User đăng nhập → `AuthService.login` sinh access token (JWT) + refresh token.
2. Mỗi request gửi kèm `Authorization: Bearer <access-token>`.
3. `JwtAuthenticationFilter`:
   - Validate token
   - Lấy email từ token, load user + roles + permissions
   - Map `permission.name` → `GrantedAuthority`
   - Set `Authentication` vào `SecurityContext`.
4. `@PreAuthorize` đọc authorities này để quyết định cho phép hay từ chối truy cập.

