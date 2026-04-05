package com.cmcu.itstudy.repository;

import com.cmcu.itstudy.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {

    Optional<Role> findByName(String name);

    boolean existsByName(String name);

    Page<Role> findByActiveTrue(Pageable pageable);

    Optional<Role> findByIdAndActiveTrue(UUID id);
}
