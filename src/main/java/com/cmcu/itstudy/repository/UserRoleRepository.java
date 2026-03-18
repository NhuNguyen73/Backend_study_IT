package com.cmcu.itstudy.repository;

import com.cmcu.itstudy.entity.UserRole;
import com.cmcu.itstudy.entity.UserRole.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {

    long countByRoleId(UUID roleId);
}
