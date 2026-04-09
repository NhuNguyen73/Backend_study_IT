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
            UUID uid = user.getId();

            roleRepository.findByName(RoleEnum.USER.name()).ifPresent(userRole -> {
                UserRole.UserRoleId userComposite = new UserRole.UserRoleId(uid, userRole.getId());
                if (userRoleRepository.existsById(userComposite)) {
                    userRoleRepository.deleteById(userComposite);
                }
            });

            Role contributorRole = roleRepository.findByName(RoleEnum.CONTRIBUTOR.name())
                    .orElseThrow(() -> new RuntimeException("Contributor role not found"));
            UserRole.UserRoleId contributorComposite = new UserRole.UserRoleId(uid, contributorRole.getId());
            if (!userRoleRepository.existsById(contributorComposite)) {
                UserRole newUserRole = UserRole.builder()
                        .userId(uid)
                        .roleId(contributorRole.getId())
                        .user(user)
                        .role(contributorRole)
                        .createdAt(LocalDateTime.now())
                        .build();
                userRoleRepository.save(newUserRole);
            }
        }
    }
}
