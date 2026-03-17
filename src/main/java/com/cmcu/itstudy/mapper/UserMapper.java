package com.cmcu.itstudy.mapper;

import com.cmcu.itstudy.dto.auth.UserInfoDto;
import com.cmcu.itstudy.entity.Permission;
import com.cmcu.itstudy.entity.Role;
import com.cmcu.itstudy.entity.User;
import com.cmcu.itstudy.entity.UserRole;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public final class UserMapper {

    private UserMapper() {
    }

    public static UserInfoDto toUserInfoDto(User user, List<Role> roles, List<Permission> permissions) {
        if (user == null) {
            return null;
        }

        List<String> roleNames = roles.stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        List<String> permissionNames = permissions.stream()
                .map(Permission::getName)
                .collect(Collectors.toList());

        return UserInfoDto.builder()
                .id(user.getId() != null ? user.getId().toString() : null)
                .email(user.getEmail())
                .fullName(user.getFullName())
                .status(user.getStatus())
                .emailVerified(user.getEmailVerified())
                .roles(roleNames)
                .permissions(permissionNames)
                .build();
    }

    public static UUID toUuid(String id) {
        return id != null ? UUID.fromString(id) : null;
    }
}

