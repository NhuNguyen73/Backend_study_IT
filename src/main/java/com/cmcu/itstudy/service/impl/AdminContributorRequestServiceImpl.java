package com.cmcu.itstudy.service.impl;

import com.cmcu.itstudy.entity.ContributorRequest;
import com.cmcu.itstudy.entity.Role;
import com.cmcu.itstudy.entity.User;
import com.cmcu.itstudy.entity.UserRole;
import com.cmcu.itstudy.enums.ContributorRequestStatus;
import com.cmcu.itstudy.enums.RoleEnum;
import com.cmcu.itstudy.repository.ContributorRequestRepository;
import com.cmcu.itstudy.repository.RoleRepository;
import com.cmcu.itstudy.repository.UserRepository;
import com.cmcu.itstudy.repository.UserRoleRepository;
import com.cmcu.itstudy.service.contract.AdminContributorRequestService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class AdminContributorRequestServiceImpl implements AdminContributorRequestService {

    private final ContributorRequestRepository contributorRequestRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;

    public AdminContributorRequestServiceImpl(
            ContributorRequestRepository contributorRequestRepository,
            UserRepository userRepository,
            RoleRepository roleRepository,
            UserRoleRepository userRoleRepository) {
        this.contributorRequestRepository = contributorRequestRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    @Transactional
    public void updateContributorRequestStatus(UUID requestId, ContributorRequestStatus newStatus, String rejectionReason) {
        ContributorRequest request = contributorRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Contributor request not found"));

        request.setStatus(newStatus);
        request.setUpdatedAt(LocalDateTime.now());

        if (newStatus == ContributorRequestStatus.REJECTED || newStatus == ContributorRequestStatus.NEED_INFO) {
            request.setRejectionReason(rejectionReason);
        } else {
            request.setRejectionReason(null);
        }

        contributorRequestRepository.save(request);

        if (newStatus == ContributorRequestStatus.APPROVED) {
            User user = userRepository.findById(request.getUser().getId())
                    .orElseThrow(() -> new RuntimeException("User not found for request"));

            Optional<Role> contributorRoleOptional = roleRepository.findByName(RoleEnum.CONTRIBUTOR.name());
            if (contributorRoleOptional.isPresent()) {
                Role contributorRole = contributorRoleOptional.get();
                // Check if the user already has the contributor role
                boolean hasContributorRole = user.getUserRoles().stream()
                        .anyMatch(userRole -> userRole.getRole() != null && userRole.getRole().getId().equals(contributorRole.getId()));

                if (!hasContributorRole) {
                    // Use the builder from UserRole entity
                    UserRole newUserRole = UserRole.builder()
                            .userId(user.getId())
                            .roleId(contributorRole.getId())
                            .user(user) // Set the user entity
                            .role(contributorRole) // Set the role entity
                            .createdAt(LocalDateTime.now())
                            .build();
                    
                    // Save the UserRole
                    userRoleRepository.save(newUserRole);
                }
            } else {
                throw new RuntimeException("Contributor role not found");
            }
        }
    }
}
