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
@Table(name = "tbl_user_roles")
@IdClass(UserRole.UserRoleId.class)
public class UserRole {

    @Id
    @Column(name = "user_id", columnDefinition = "uniqueidentifier")
    private UUID userId;

    @Id
    @Column(name = "role_id", columnDefinition = "uniqueidentifier")
    private UUID roleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", insertable = false, updatable = false)
    private Role role;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserRoleId implements Serializable {

        private UUID userId;
        private UUID roleId;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            UserRoleId that = (UserRoleId) o;
            if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
            return roleId != null ? roleId.equals(that.roleId) : that.roleId == null;
        }

        @Override
        public int hashCode() {
            int result = userId != null ? userId.hashCode() : 0;
            result = 31 * result + (roleId != null ? roleId.hashCode() : 0);
            return result;
        }
    }
}

