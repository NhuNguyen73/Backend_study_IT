package com.cmcu.itstudy.service.impl;

import com.cmcu.itstudy.dto.admin.AdminContributorCertificateDto;
import com.cmcu.itstudy.dto.admin.AdminContributorRequestDto;
import com.cmcu.itstudy.entity.ContributorRequest;
import com.cmcu.itstudy.entity.User;
import com.cmcu.itstudy.repository.ContributorRequestRepository;
import com.cmcu.itstudy.service.contract.ContributorRequestService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import com.cmcu.itstudy.entity.ContributorCertificate; // <-- Thêm import này

@Service
public class ContributorRequestServiceImpl implements ContributorRequestService {

    private final ContributorRequestRepository contributorRequestRepository;

    public ContributorRequestServiceImpl(ContributorRequestRepository contributorRequestRepository) {
        this.contributorRequestRepository = contributorRequestRepository;
    }

    @Override
    public List<AdminContributorRequestDto> getAllContributorRequestsForAdmin() {
        List<ContributorRequest> requests = contributorRequestRepository.findAllWithUserAndCertificates();

        return requests.stream()
            .map(req -> {
                User user = req.getUser(); 
                
                List<ContributorCertificate> certificates = req.getCertificates();
                List<AdminContributorCertificateDto> certificatesDto = Collections.emptyList();

                if (certificates != null && !certificates.isEmpty()) {
                    certificatesDto = certificates.stream()
                        .filter(cert -> cert != null)
                        .map(cert -> AdminContributorCertificateDto.builder()
                            .url(cert.getCertificateUrl()) 
                            .certificateName(cert.getCertificateName())
                            .build()) 
                        .collect(Collectors.toList());
                } else if (req.getCertificateUrl() != null && req.getCertificateName() != null) {
                    certificatesDto = Collections.singletonList(AdminContributorCertificateDto.builder()
                        .url(req.getCertificateUrl())
                        .certificateName(req.getCertificateName())
                        .build());
                }

                return AdminContributorRequestDto.builder()
                    .id(req.getId())
                    .userId(user != null ? user.getId() : null)
                    .name(user != null ? user.getFullName() : "N/A") 
                    .email(user != null ? user.getEmail() : "N/A")
                    .portfolioLink(req.getPortfolioLink())
                    .experience(req.getExperience())
                    .createdAt(req.getCreatedAt())
                    .status(req.getStatus())
                    .certificates(certificatesDto)
                    .avatarUrl(null) 
                    .build();
            })
            .collect(Collectors.toList());
    }
}
