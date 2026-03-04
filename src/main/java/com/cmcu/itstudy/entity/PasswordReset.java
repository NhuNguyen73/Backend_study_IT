package com.cmcu.itstudy.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.cmcu.itstudy.enums.TokenType;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_password_resets")
public class PasswordReset {

    @Id
    @Column(name = "pr_id", nullable = false, columnDefinition = "uniqueidentifier")
    private UUID id;

    @Version
    @Column(name = "pr_version", nullable = false)
    private Long version;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pr_u_id", nullable = false)
    private User user;

    @Column(name = "pr_token", nullable = false, length = 500)
    private String token;

    @Column(name = "pr_type", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private TokenType type;

    @Column(name = "pr_is_used", nullable = false, columnDefinition = "bit")
    private Boolean used;

    @Column(name = "pr_expires_at", nullable = false, columnDefinition = "datetime2")
    private LocalDateTime expiresAt;

    @Column(name = "pr_created_at", nullable = false, columnDefinition = "datetime2")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (id == null) id = UUID.randomUUID();
        if (createdAt == null) createdAt = LocalDateTime.now();
    }
}

