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
@Table(name = "tbl_authors")
public class Author {

    @Id
    @Column(name = "a_id", nullable = false, columnDefinition = "uniqueidentifier")
    private UUID id;

    @Column(name = "a_full_name", length = 255)
    private String fullName;

    @Column(name = "a_email", length = 255)
    private String email;

    @Column(name = "a_affiliation", length = 255)
    private String affiliation;

    @Column(name = "a_bio", columnDefinition = "text")
    private String bio;

    @Column(name = "a_avatar", length = 500)
    private String avatar;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "a_u_id")
    private User user;

    @Column(name = "a_created_at", columnDefinition = "datetime2")
    private LocalDateTime createdAt;

    @Column(name = "a_updated_at", columnDefinition = "datetime2")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        if (id == null) id = UUID.randomUUID();
        if (createdAt == null) createdAt = LocalDateTime.now();
    }
}