package com.cmcu.itstudy.service.impl;

import com.cmcu.itstudy.dto.document.DocumentCardResponseDto;
import com.cmcu.itstudy.dto.document.HomepageStatisticsResponseDto;
import com.cmcu.itstudy.entity.Document;
import com.cmcu.itstudy.entity.Role;
import com.cmcu.itstudy.enums.DocumentStatus;
import com.cmcu.itstudy.mapper.DocumentMapper;
import com.cmcu.itstudy.repository.DocumentRepository;
import com.cmcu.itstudy.repository.RoleRepository;
import com.cmcu.itstudy.repository.UserRepository;
import com.cmcu.itstudy.repository.UserRoleRepository;
import com.cmcu.itstudy.service.contract.HomepageService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HomepageServiceImpl implements HomepageService {

    private static final String USER_STATUS_ACTIVE = "ACTIVE";
    private static final String ROLE_CONTRIBUTOR = "CONTRIBUTOR";

    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final DocumentCardEnrichmentService documentCardEnrichmentService;

    public HomepageServiceImpl(DocumentRepository documentRepository,
                               UserRepository userRepository,
                               RoleRepository roleRepository,
                               UserRoleRepository userRoleRepository,
                               DocumentCardEnrichmentService documentCardEnrichmentService) {
        this.documentRepository = documentRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
        this.documentCardEnrichmentService = documentCardEnrichmentService;
    }

    @Transactional(readOnly = true)
    @Override
    public HomepageStatisticsResponseDto getStatistics() {
        long totalApprovedDocuments = documentRepository.countByStatusAndDeletedFalse(DocumentStatus.APPROVED);
        long totalActiveUsers = userRepository.countByStatus(USER_STATUS_ACTIVE);
        long totalDownloads = documentRepository.sumDownloadCountByStatusAndDeletedFalse(DocumentStatus.APPROVED);

        long totalContributors = 0L;
        Optional<Role> contributorRole = roleRepository.findByName(ROLE_CONTRIBUTOR);
        if (contributorRole.isPresent()) {
            totalContributors = userRoleRepository.countByRoleId(contributorRole.get().getId());
        }

        return HomepageStatisticsResponseDto.builder()
                .totalApprovedDocuments((int) totalApprovedDocuments)
                .totalActiveUsers((int) totalActiveUsers)
                .totalDownloads((int) totalDownloads)
                .totalContributors((int) totalContributors)
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public List<DocumentCardResponseDto> getLatestDocuments(int limit, java.util.UUID currentUserId) {
        PageRequest pageRequest = PageRequest.of(0, limit);
        List<Document> documents = documentRepository
                .findByStatusAndDeletedFalseOrderByCreatedAtDesc(DocumentStatus.APPROVED, pageRequest)
                .getContent();

        return documentCardEnrichmentService.toEnrichedCardDtos(documents, currentUserId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<DocumentCardResponseDto> getTrendingDocuments(int limit, java.util.UUID currentUserId) {
        LocalDateTime from = LocalDateTime.now().minus(7, ChronoUnit.DAYS);
        PageRequest pageRequest = PageRequest.of(0, limit);

        List<Document> documents = documentRepository.findTrendingByViews(DocumentStatus.APPROVED, from, pageRequest);
        if (documents.isEmpty()) {
            documents = documentRepository.findTrendingByDownloads(DocumentStatus.APPROVED, from, pageRequest);
        }

        return documentCardEnrichmentService.toEnrichedCardDtos(documents, currentUserId);
    }
}

