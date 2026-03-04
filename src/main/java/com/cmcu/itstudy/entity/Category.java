package com.cmcu.itstudy.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Table(name = "tbl_categories")
public class Category {

    @Id
    @Column(name = "c_id", nullable = false, columnDefinition = "uniqueidentifier")
    private UUID id;

    @Column(name = "c_name", length = 255)
    private String name;

    @Column(name = "c_description", columnDefinition = "text")
    private String description;

    @Column(name = "c_created_at", columnDefinition = "datetime2")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (id == null) id = UUID.randomUUID();
        if (createdAt == null) createdAt = LocalDateTime.now();
    }
}