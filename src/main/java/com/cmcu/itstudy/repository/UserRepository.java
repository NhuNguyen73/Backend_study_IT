package com.cmcu.itstudy.repository;

import com.cmcu.itstudy.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    // New method to fetch user with roles
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.userRoles ur LEFT JOIN FETCH ur.role WHERE u.email = :email")
    Optional<User> findByEmailWithRoles(@Param("email") String email);

    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.userRoles ur LEFT JOIN FETCH ur.role WHERE u.id = :id")
    Optional<User> findByIdWithRoles(@Param("id") UUID id);

    @Query(
            value = "SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.userRoles ur LEFT JOIN FETCH ur.role WHERE "
                    + "(:search IS NULL OR :search = '' OR LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%')) OR "
                    + "(u.fullName IS NOT NULL AND LOWER(u.fullName) LIKE LOWER(CONCAT('%', :search, '%'))))",
            countQuery = "SELECT COUNT(u) FROM User u WHERE "
                    + "(:search IS NULL OR :search = '' OR LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%')) OR "
                    + "(u.fullName IS NOT NULL AND LOWER(u.fullName) LIKE LOWER(CONCAT('%', :search, '%'))))"
    )
    Page<User> searchForAdmin(@Param("search") String search, Pageable pageable);

    boolean existsByEmail(String email);

    long countByStatus(String status);
}

