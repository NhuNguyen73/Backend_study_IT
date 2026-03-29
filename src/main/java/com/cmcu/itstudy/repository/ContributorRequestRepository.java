package com.cmcu.itstudy.repository;

import com.cmcu.itstudy.entity.ContributorRequest;
import com.cmcu.itstudy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ContributorRequestRepository extends JpaRepository<ContributorRequest, UUID> {
    boolean existsByUserAndStatus(User user, String status);
    Optional<ContributorRequest> findFirstByUserOrderByCreatedAtDesc(User user);
}
