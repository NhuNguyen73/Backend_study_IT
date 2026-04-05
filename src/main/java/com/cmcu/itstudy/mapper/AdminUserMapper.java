package com.cmcu.itstudy.mapper;

import com.cmcu.itstudy.dto.admin.user.AdminUserResponseDto;
import com.cmcu.itstudy.dto.admin.user.AdminUserRoleDto;
import com.cmcu.itstudy.entity.Role;
import com.cmcu.itstudy.entity.User;
import com.cmcu.itstudy.entity.UserRole;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public final class AdminUserMapper {

    private AdminUserMapper() {
    }

    public static AdminUserResponseDto toResponseDto(User user) {
        if (user == null) {
            return null;
        }
        return AdminUserResponseDto.builder()
                .id(user.getId() != null ? user.getId().toString() : null)
                .email(user.getEmail())
                .fullName(user.getFullName())
                .avatar(user.getAvatar())
                .status(user.getStatus())
                .emailVerified(user.getEmailVerified())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .roles(mapRoles(user))
                .build();
    }

    private static List<AdminUserRoleDto> mapRoles(User user) {
        if (user.getUserRoles() == null || user.getUserRoles().isEmpty()) {
            return Collections.emptyList();
        }
        return user.getUserRoles().stream()
                .map(UserRole::getRole)
                .filter(r -> r != null)
                .sorted(Comparator.comparing(Role::getName, Comparator.nullsLast(String::compareToIgnoreCase)))
                .map(r -> AdminUserRoleDto.builder()
                        .id(r.getId() != null ? r.getId().toString() : null)
                        .name(r.getName())
                        .description(r.getDescription())
                        .build())
                .collect(Collectors.toList());
    }
}
