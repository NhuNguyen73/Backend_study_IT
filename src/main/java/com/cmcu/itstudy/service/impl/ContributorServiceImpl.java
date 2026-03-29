package com.cmcu.itstudy.service.impl;

import com.cmcu.itstudy.dto.contributor.ContributorRegistrationRequestDto;
import com.cmcu.itstudy.dto.contributor.ContributorStatusDto;
import com.cmcu.itstudy.entity.ContributorCertificate;
import com.cmcu.itstudy.entity.ContributorRequest;
import com.cmcu.itstudy.entity.User;
import com.cmcu.itstudy.repository.ContributorRequestRepository;
import com.cmcu.itstudy.repository.UserRepository;
import com.cmcu.itstudy.service.contract.ContributorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ContributorServiceImpl implements ContributorService {

    private final ContributorRequestRepository contributorRequestRepository;
    private final UserRepository userRepository;

    public ContributorServiceImpl(ContributorRequestRepository contributorRequestRepository, UserRepository userRepository) {
        this.contributorRequestRepository = contributorRequestRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void registerContributor(ContributorRegistrationRequestDto request, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (contributorRequestRepository.existsByUserAndStatus(user, "PENDING")) {
            throw new RuntimeException("Bạn đã có một yêu cầu đang chờ duyệt.");
        }

        ContributorRequest contributorRequest = ContributorRequest.builder()
                .user(user)
                .portfolioLink(request.getPortfolioLink())
                .experience(request.getExperience())
                .status("PENDING")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        if (!request.getCertificates().isEmpty()) {
            contributorRequest.setCertificateName(request.getCertificates().get(0).getCertificateName());
            contributorRequest.setCertificateUrl(request.getCertificates().get(0).getUrl());
        }

        List<ContributorCertificate> certificates = request.getCertificates().stream()
                .map(certDto -> ContributorCertificate.builder()
                        .request(contributorRequest)
                        .certificateUrl(certDto.getUrl())
                        .certificateName(certDto.getCertificateName())
                        .build())
                .collect(Collectors.toList());

        contributorRequest.setCertificates(certificates);
        contributorRequestRepository.save(contributorRequest);
    }

    @Override
    public ContributorStatusDto getRegistrationStatus(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<ContributorRequest> latestRequest = contributorRequestRepository.findFirstByUserOrderByCreatedAtDesc(user);

        if (latestRequest.isEmpty()) {
            return null;
        }

        ContributorRequest request = latestRequest.get();
        return ContributorStatusDto.builder()
                .status(request.getStatus())
                .rejectionReason(request.getRejectionReason())
                .createdAt(request.getCreatedAt())
                .updatedAt(request.getUpdatedAt())
                .build();
    }
}
