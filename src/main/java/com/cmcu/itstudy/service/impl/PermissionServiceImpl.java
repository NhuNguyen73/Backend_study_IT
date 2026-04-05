package com.cmcu.itstudy.service.impl;

import com.cmcu.itstudy.dto.admin.permission.AdminPermissionResponseDto;
import com.cmcu.itstudy.entity.Permission;
import com.cmcu.itstudy.mapper.AdminPermissionMapper;
import com.cmcu.itstudy.repository.PermissionRepository;
import com.cmcu.itstudy.service.contract.PermissionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;

    public PermissionServiceImpl(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdminPermissionResponseDto> listAllPermissions() {
        return permissionRepository.findAllByOrderByNameAsc().stream()
                .map(AdminPermissionMapper::toDto)
                .collect(Collectors.toList());
    }
}
