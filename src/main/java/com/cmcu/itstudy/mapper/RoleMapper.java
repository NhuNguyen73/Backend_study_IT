package com.cmcu.itstudy.mapper;

import com.cmcu.itstudy.dto.role.RoleDto;
import com.cmcu.itstudy.entity.Role;

import java.util.UUID;

public final class RoleMapper {

    private RoleMapper() {
    }

    public static RoleDto toRoleDto(Role role) {
        if (role == null) {
            return null;
        }

        return RoleDto.builder()
                .id(role.getId() != null ? role.getId().toString() : null)
                .name(role.getName())
                .description(role.getDescription())
                .build();
    }

    public static Role toRoleEntity(RoleDto dto) {
        if (dto == null) {
            return null;
        }

        Role role = new Role();
        if (dto.getId() != null) {
            role.setId(UUID.fromString(dto.getId()));
        }
        role.setName(dto.getName());
        role.setDescription(dto.getDescription());
        return role;
    }
}

