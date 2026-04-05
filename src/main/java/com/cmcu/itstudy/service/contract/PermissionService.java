package com.cmcu.itstudy.service.contract;

import com.cmcu.itstudy.dto.admin.permission.AdminPermissionResponseDto;

import java.util.List;

public interface PermissionService {

    List<AdminPermissionResponseDto> listAllPermissions();
}
