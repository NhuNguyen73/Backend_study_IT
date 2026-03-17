package com.cmcu.itstudy.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_role_permissions")
@IdClass(RolePermission.RolePermissionId.class)
public class RolePermission {

    @Id
    @Column(name = "role_id", columnDefinition = "uniqueidentifier")
    private UUID roleId;

    @Id
    @Column(name = "permission_id", columnDefinition = "uniqueidentifier")
    private UUID permissionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", insertable = false, updatable = false)
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permission_id", insertable = false, updatable = false)
    private Permission permission;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RolePermissionId implements Serializable {

        private UUID roleId;
        private UUID permissionId;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            RolePermissionId that = (RolePermissionId) o;
            if (roleId != null ? !roleId.equals(that.roleId) : that.roleId != null) return false;
            return permissionId != null ? permissionId.equals(that.permissionId) : that.permissionId == null;
        }

        @Override
        public int hashCode() {
            int result = roleId != null ? roleId.hashCode() : 0;
            result = 31 * result + (permissionId != null ? permissionId.hashCode() : 0);
            return result;
        }
    }
}

