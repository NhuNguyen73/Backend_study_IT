package com.cmcu.itstudy.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.cmcu.itstudy.enums.UserStatus;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_users")
public class User {

    @Id
    @Column(name = "u_id", nullable = false, columnDefinition = "uniqueidentifier")
    private UUID id;

    @Column(name = "u_email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "u_password", nullable = false, length = 255)
    private String password;

    @Column(name = "u_full_name", length = 255)
    private String fullName;

    @Column(name = "u_avatar", length = 500)
    private String avatar;

    @Column(name = "u_status", length = 50)
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Column(name = "u_email_verified", nullable = false, columnDefinition = "bit")
    private Boolean emailVerified;

    @Column(name = "u_last_login_at", columnDefinition = "datetime2")
    private LocalDateTime lastLoginAt;

    @Column(name = "u_lock_until", columnDefinition = "datetime2")
    private LocalDateTime lockUntil;

    @Column(name = "u_deleted_at", columnDefinition = "datetime2")
    private LocalDateTime deletedAt;

    @Column(name = "u_created_at", nullable = false, columnDefinition = "datetime2")
    private LocalDateTime createdAt;

    @Column(name = "u_updated_at", columnDefinition = "datetime2")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        if (id == null) id = UUID.randomUUID();
        if (createdAt == null) createdAt = LocalDateTime.now();
    }
}