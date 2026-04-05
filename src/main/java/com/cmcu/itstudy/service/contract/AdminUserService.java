package com.cmcu.itstudy.service.contract;

import com.cmcu.itstudy.dto.admin.user.AdminAssignRoleRequestDto;
import com.cmcu.itstudy.dto.admin.user.AdminUserCreateRequestDto;
import com.cmcu.itstudy.dto.admin.user.AdminUserPageResponseDto;
import com.cmcu.itstudy.dto.admin.user.AdminUserResponseDto;
import com.cmcu.itstudy.dto.admin.user.AdminUserStatusPatchRequestDto;
import com.cmcu.itstudy.dto.admin.user.AdminUserUpdateRequestDto;

import java.util.UUID;

public interface AdminUserService {

    AdminUserPageResponseDto listUsers(int page, int size, String search);

    AdminUserResponseDto getUser(UUID id);

    AdminUserResponseDto createUser(AdminUserCreateRequestDto request);

    AdminUserResponseDto updateUser(UUID id, AdminUserUpdateRequestDto request);

    AdminUserResponseDto patchStatus(UUID id, AdminUserStatusPatchRequestDto request);

    AdminUserResponseDto assignRole(UUID userId, AdminAssignRoleRequestDto request);

    void removeRole(UUID userId, UUID roleId);
}
