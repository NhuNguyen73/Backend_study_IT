package com.cmcu.itstudy.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.FetchType;
import jakarta.persistence.PrePersist;
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
@Table(name = "tbl_user_roles")
public class UserRole {

    @Id
    @Column(name = "ur_id", nullable = false, columnDefinition = "uniqueidentifier")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ur_u_id", nullable = false)
    private User user;

    // bạn sẽ tạo Role entity sau
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ur_r_id", nullable = false)
    private Role role;

    @Column(name = "ur_assigned_at", columnDefinition = "datetime2")
    private LocalDateTime assignedAt;

    @PrePersist
    public void prePersist() {
        if (id == null) id = UUID.randomUUID();
        if (assignedAt == null) assignedAt = LocalDateTime.now();
    }
}