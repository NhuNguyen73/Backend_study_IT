package com.cmcu.itstudy.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.PrePersist;
import jakarta.persistence.FetchType;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_refresh_tokens")
public class RefreshToken {

    @Id
    @Column(name = "rt_id", nullable = false, columnDefinition = "uniqueidentifier")
    private UUID id;

    @Version
    @Column(name = "rt_version", nullable = false)
    private Long version;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rt_u_id", nullable = false)
    private User user;

    @Column(name = "rt_token", nullable = false, length = 500)
    private String token;

    @Column(name = "rt_is_revoked", nullable = false, columnDefinition = "bit")
    private Boolean revoked;

    @Column(name = "rt_expires_at", nullable = false, columnDefinition = "datetime2")
    private LocalDateTime expiresAt;

    @Column(name = "rt_created_at", nullable = false, columnDefinition = "datetime2")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (id == null) id = UUID.randomUUID();
        if (createdAt == null) createdAt = LocalDateTime.now();
    }
}

