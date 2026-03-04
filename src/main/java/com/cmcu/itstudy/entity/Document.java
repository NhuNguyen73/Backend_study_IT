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
@Table(name = "tbl_documents")
public class Document {

    @Id
    @Column(name = "doc_id", nullable = false, columnDefinition = "uniqueidentifier")
    private UUID id;

    @Column(name = "doc_title", length = 255)
    private String title;

    @Column(name = "doc_description", columnDefinition = "text")
    private String description;

    @Column(name = "doc_thumbnail", length = 500)
    private String thumbnail;

    @Column(name = "doc_file_path", length = 500)
    private String filePath;

    @Column(name = "doc_status", length = 50)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doc_owner_u_id")
    private User owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doc_c_id")
    private Category category;

    @Column(name = "doc_created_at", columnDefinition = "datetime2")
    private LocalDateTime createdAt;

    @Column(name = "doc_updated_at", columnDefinition = "datetime2")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        if (id == null) id = UUID.randomUUID();
        if (createdAt == null) createdAt = LocalDateTime.now();
    }
}