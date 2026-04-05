package com.cmcu.itstudy.service.contract;

import com.cmcu.itstudy.dto.admin.permission.AdminPermissionResponseDto;
import com.cmcu.itstudy.dto.admin.role.AdminRoleCreateRequestDto;
import com.cmcu.itstudy.dto.admin.role.AdminRolePageResponseDto;
import com.cmcu.itstudy.dto.admin.role.AdminRoleResponseDto;
import com.cmcu.itstudy.dto.admin.role.AdminRoleStatusPatchRequestDto;
import com.cmcu.itstudy.dto.admin.role.AdminRoleUpdateRequestDto;

import java.util.List;
import java.util.UUID;

public interface RoleService {

    AdminRolePageResponseDto listRoles(int page, int size);

    AdminRoleResponseDto getRole(UUID id);

    AdminRoleResponseDto createRole(AdminRoleCreateRequestDto request);

    AdminRoleResponseDto updateRole(UUID id, AdminRoleUpdateRequestDto request);

    AdminRoleResponseDto patchStatus(UUID id, AdminRoleStatusPatchRequestDto request);

    List<AdminPermissionResponseDto> getRolePermissions(UUID roleId);

    void addPermissionsToRole(UUID roleId, List<UUID> permissionIds);

    void removePermissionsFromRole(UUID roleId, List<UUID> permissionIds);
}
