package com.cmcu.itstudy.repository;

import com.cmcu.itstudy.entity.RolePermission;
import com.cmcu.itstudy.entity.RolePermission.RolePermissionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface RolePermissionRepository extends JpaRepository<RolePermission, RolePermissionId> {

    @Query("SELECT rp FROM RolePermission rp JOIN FETCH rp.permission WHERE rp.roleId = :roleId ORDER BY rp.permission.name")
    List<RolePermission> findByRoleIdWithPermission(@Param("roleId") UUID roleId);

    void deleteByRoleId(UUID roleId);

    void deleteByRoleIdAndPermissionIdIn(UUID roleId, Collection<UUID> permissionIds);
}
