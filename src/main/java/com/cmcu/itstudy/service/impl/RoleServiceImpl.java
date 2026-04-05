package com.cmcu.itstudy.service.impl;

import com.cmcu.itstudy.dto.admin.permission.AdminPermissionResponseDto;
import com.cmcu.itstudy.dto.admin.role.AdminRoleCreateRequestDto;
import com.cmcu.itstudy.dto.admin.role.AdminRolePageResponseDto;
import com.cmcu.itstudy.dto.admin.role.AdminRoleResponseDto;
import com.cmcu.itstudy.dto.admin.role.AdminRoleStatusPatchRequestDto;
import com.cmcu.itstudy.dto.admin.role.AdminRoleUpdateRequestDto;
import com.cmcu.itstudy.entity.Role;
import com.cmcu.itstudy.entity.RolePermission;
import com.cmcu.itstudy.mapper.AdminPermissionMapper;
import com.cmcu.itstudy.mapper.AdminRoleMapper;
import com.cmcu.itstudy.repository.PermissionRepository;
import com.cmcu.itstudy.repository.RolePermissionRepository;
import com.cmcu.itstudy.repository.RoleRepository;
import com.cmcu.itstudy.service.contract.RoleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int MAX_PAGE_SIZE = 100;

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;

    public RoleServiceImpl(
            RoleRepository roleRepository,
            PermissionRepository permissionRepository,
            RolePermissionRepository rolePermissionRepository
    ) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.rolePermissionRepository = rolePermissionRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public AdminRolePageResponseDto listRoles(int page, int size) {
        int p = Math.max(0, page);
        int s = size > 0 ? Math.min(size, MAX_PAGE_SIZE) : DEFAULT_PAGE_SIZE;
        Pageable pageable = PageRequest.of(p, s, Sort.by(Sort.Direction.ASC, "name"));
        Page<Role> result = roleRepository.findByActiveTrue(pageable);
        List<AdminRoleResponseDto> content = result.getContent().stream()
                .map(AdminRoleMapper::toResponseDto)
                .collect(Collectors.toList());
        return AdminRolePageResponseDto.builder()
                .content(content)
                .page(result.getNumber())
                .size(result.getSize())
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public AdminRoleResponseDto getRole(UUID id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));
        return AdminRoleMapper.toResponseDto(role);
    }

    @Override
    @Transactional
    public AdminRoleResponseDto createRole(AdminRoleCreateRequestDto request) {
        String name = request.getName().trim();
        if (roleRepository.existsByName(name)) {
            throw new IllegalArgumentException("Role name already exists");
        }
        LocalDateTime now = LocalDateTime.now();
        Role role = Role.builder()
                .name(name)
                .description(trimToNull(request.getDescription()))
                .active(Boolean.TRUE)
                .createdAt(now)
                .updatedAt(now)
                .build();
        role = roleRepository.save(role);
        return AdminRoleMapper.toResponseDto(role);
    }

    @Override
    @Transactional
    public AdminRoleResponseDto updateRole(UUID id, AdminRoleUpdateRequestDto request) {
        boolean hasChange = (request.getName() != null && !request.getName().isBlank())
                || request.getDescription() != null;
        if (!hasChange) {
            throw new IllegalArgumentException("No fields to update");
        }

        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));
        if (Boolean.FALSE.equals(role.getActive())) {
            throw new IllegalArgumentException("Cannot update an inactive role; restore it first");
        }

        if (request.getName() != null && !request.getName().isBlank()) {
            String newName = request.getName().trim();
            if (!newName.equals(role.getName()) && roleRepository.existsByName(newName)) {
                throw new IllegalArgumentException("Role name already exists");
            }
            role.setName(newName);
        }
        if (request.getDescription() != null) {
            role.setDescription(trimToNull(request.getDescription()));
        }
        role.setUpdatedAt(LocalDateTime.now());
        roleRepository.save(role);
        return AdminRoleMapper.toResponseDto(role);
    }

    @Override
    @Transactional
    public AdminRoleResponseDto patchStatus(UUID id, AdminRoleStatusPatchRequestDto request) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));
        role.setActive(request.getActive());
        role.setUpdatedAt(LocalDateTime.now());
        roleRepository.save(role);
        return AdminRoleMapper.toResponseDto(role);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdminPermissionResponseDto> getRolePermissions(UUID roleId) {
        roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));
        return rolePermissionRepository.findByRoleIdWithPermission(roleId).stream()
                .map(RolePermission::getPermission)
                .filter(Objects::nonNull)
                .map(AdminPermissionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void addPermissionsToRole(UUID roleId, List<UUID> permissionIds) {
        roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));
        LocalDateTime now = LocalDateTime.now();
        for (UUID permissionId : normalizePermissionIds(permissionIds)) {
            permissionRepository.findById(permissionId)
                    .orElseThrow(() -> new IllegalArgumentException("Permission not found: " + permissionId));
            RolePermission.RolePermissionId compositeId = new RolePermission.RolePermissionId(roleId, permissionId);
            if (!rolePermissionRepository.existsById(compositeId)) {
                rolePermissionRepository.save(RolePermission.builder()
                        .roleId(roleId)
                        .permissionId(permissionId)
                        .createdAt(now)
                        .build());
            }
        }
    }

    @Override
    @Transactional
    public void removePermissionsFromRole(UUID roleId, List<UUID> permissionIds) {
        roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));
        List<UUID> ids = normalizePermissionIds(permissionIds);
        if (ids.isEmpty()) {
            rolePermissionRepository.deleteByRoleId(roleId);
        } else {
            rolePermissionRepository.deleteByRoleIdAndPermissionIdIn(roleId, ids);
        }
    }

    private static List<UUID> normalizePermissionIds(List<UUID> permissionIds) {
        if (permissionIds == null || permissionIds.isEmpty()) {
            return List.of();
        }
        return permissionIds.stream()
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
    }

    private static String trimToNull(String s) {
        if (s == null) {
            return null;
        }
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }
}
