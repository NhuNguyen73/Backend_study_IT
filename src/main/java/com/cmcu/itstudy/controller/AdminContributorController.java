package com.cmcu.itstudy.controller;

import com.cmcu.itstudy.dto.admin.AdminContributorCertificateDto;
import com.cmcu.itstudy.dto.admin.AdminContributorRequestDto;
import com.cmcu.itstudy.dto.common.ApiResponse;
import com.cmcu.itstudy.entity.ContributorRequest;
import com.cmcu.itstudy.entity.ContributorCertificate;
import com.cmcu.itstudy.entity.User;
import com.cmcu.itstudy.repository.ContributorRequestRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminContributorController {

    private final ContributorRequestRepository contributorRequestRepository;

    public AdminContributorController(ContributorRequestRepository contributorRequestRepository) {
        this.contributorRequestRepository = contributorRequestRepository;
    }

    @GetMapping("/contributor-requests")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER_MODERATOR')")
    public ResponseEntity<ApiResponse<List<AdminContributorRequestDto>>> getAllContributorRequests() {
        try {
            List<ContributorRequest> requests = contributorRequestRepository.findAllWithUserAndCertificates();

            List<AdminContributorRequestDto> adminRequestsDto = requests.stream()
                .map(req -> {
                    User user = req.getUser(); 
                    String userName = (user != null) ? user.getFullName() : "N/A";
                    String userEmail = (user != null) ? user.getEmail() : "N/A";

                    List<ContributorCertificate> certificates = req.getCertificates();
                    List<AdminContributorCertificateDto> certificatesDto = Collections.emptyList();

                    if (certificates != null && !certificates.isEmpty()) {
                        certificatesDto = certificates.stream()
                            .filter(Objects::nonNull) 
                            .map(cert -> {
                                if (cert == null) return null; 
                                return AdminContributorCertificateDto.builder()
                                    .url(cert.getCertificateUrl()) 
                                    .certificateName(cert.getCertificateName())
                                    .build();
                            })
                            .filter(Objects::nonNull) 
                            .collect(Collectors.toList());
                    } else if (req.getCertificateUrl() != null && req.getCertificateName() != null) {
                        certificatesDto = Collections.singletonList(AdminContributorCertificateDto.builder()
                            .url(req.getCertificateUrl())
                            .certificateName(req.getCertificateName())
                            .build());
                    }

                    return AdminContributorRequestDto.builder()
                        .id(req.getId())
                        .name(userName) 
                        .email(userEmail)
                        .portfolioLink(req.getPortfolioLink())
                        .experience(req.getExperience())
                        .createdAt(req.getCreatedAt())
                        .status(req.getStatus())
                        .certificates(certificatesDto)
                        .avatarUrl(null) 
                        .build();
                })
                .collect(Collectors.toList());

            return ResponseEntity.ok(ApiResponse.success(adminRequestsDto, "Lấy danh sách yêu cầu Contributor thành công."));
        } catch (Exception e) {
            System.err.println("Error fetching contributor requests for admin: " + e.getMessage());
            e.printStackTrace(); 
            return ResponseEntity.status(500).body(ApiResponse.failure("Có lỗi xảy ra khi lấy danh sách yêu cầu."));
        }
    }
}
