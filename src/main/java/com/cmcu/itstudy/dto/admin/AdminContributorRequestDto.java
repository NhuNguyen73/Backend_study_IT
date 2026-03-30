package com.cmcu.itstudy.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminContributorRequestDto {
    private UUID id;
    private String name; // Tên người dùng gửi yêu cầu
    private String email; // Email người dùng gửi yêu cầu
    private String portfolioLink;
    private String experience;
    private LocalDateTime createdAt;
    private String status; // Trạng thái: PENDING, APPROVED, REJECTED
    private List<AdminContributorCertificateDto> certificates;
    private String avatarUrl; // URL avatar của người dùng
}
